package uv.lis.modelo.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import uv.lis.modelo.Cliente;
import uv.lis.modelo.CuentaAhorros;
import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.CuentaCorriente;
import uv.lis.modelo.CuentaEmpresarial;
import uv.lis.modelo.TipoCuenta;

public class MapeadorCuenta {

    public CuentaBancaria mapearDesdeFila(ResultSet fila) throws SQLException {
        TipoCuenta tipo = TipoCuenta.valueOf(fila.getString("tipo"));
        CuentaBancaria cuenta;
        switch (tipo) {
            case AHORROS:
                cuenta = construirAhorros(fila);
                break;
            case CORRIENTE:
                cuenta = construirCorriente(fila);
                break;
            default:
                cuenta = construirEmpresarial(fila);
                break;
        }
        return cuenta;
    }

    public void asignarParametrosInsercion(PreparedStatement sentencia, CuentaBancaria cuenta)
            throws SQLException {
        sentencia.setString(1, cuenta.getNumeroCuenta());
        asignarColumnasModificables(sentencia, cuenta, 2);
    }

    public void asignarParametrosActualizacion(PreparedStatement sentencia, CuentaBancaria cuenta)
            throws SQLException {
        int indiceClave = asignarColumnasModificables(sentencia, cuenta, 1);
        sentencia.setString(indiceClave, cuenta.getNumeroCuenta());
    }

    private int asignarColumnasModificables(PreparedStatement sentencia, CuentaBancaria cuenta, int inicio)
            throws SQLException {
        int indice = inicio;
        sentencia.setString(indice++, cuenta.obtenerTipoCuenta().name());
        sentencia.setDouble(indice++, cuenta.getSaldo());
        asignarRfcCliente(sentencia, cuenta, indice++);
        asignarIdSucursal(sentencia, cuenta, indice++);
        indice = asignarColumnasEspecificas(sentencia, cuenta, indice);
        return indice;
    }

    private int asignarColumnasEspecificas(PreparedStatement sentencia, CuentaBancaria cuenta, int inicio)
            throws SQLException {
        switch (cuenta.obtenerTipoCuenta()) {
            case AHORROS:
                asignarEspecificasAhorros(sentencia, (CuentaAhorros) cuenta, inicio);
                break;
            case CORRIENTE:
                asignarEspecificasCorriente(sentencia, (CuentaCorriente) cuenta, inicio);
                break;
            default:
                asignarEspecificasEmpresarial(sentencia, (CuentaEmpresarial) cuenta, inicio);
                break;
        }
        int indiceSiguiente = inicio + 3;
        return indiceSiguiente;
    }

    private void asignarEspecificasAhorros(PreparedStatement sentencia, CuentaAhorros cuenta, int inicio)
            throws SQLException {
        sentencia.setDouble(inicio, cuenta.getTasaInteres());
        sentencia.setNull(inicio + 1, Types.DECIMAL);
        sentencia.setNull(inicio + 2, Types.VARCHAR);
    }

    private void asignarEspecificasCorriente(PreparedStatement sentencia, CuentaCorriente cuenta, int inicio)
            throws SQLException {
        sentencia.setNull(inicio, Types.DECIMAL);
        sentencia.setDouble(inicio + 1, cuenta.getLimiteCredito());
        sentencia.setNull(inicio + 2, Types.VARCHAR);
    }

    private void asignarEspecificasEmpresarial(PreparedStatement sentencia, CuentaEmpresarial cuenta, int inicio)
            throws SQLException {
        sentencia.setNull(inicio, Types.DECIMAL);
        sentencia.setDouble(inicio + 1, cuenta.getLimiteCredito());
        sentencia.setString(inicio + 2, cuenta.getRazonSocial());
    }

    private void asignarRfcCliente(PreparedStatement sentencia, CuentaBancaria cuenta, int indice)
            throws SQLException {
        Cliente cliente = cuenta.getCliente();
        if (cliente != null && cliente.getRfcCurp() != null) {
            sentencia.setString(indice, cliente.getRfcCurp());
        } else {
            sentencia.setNull(indice, Types.VARCHAR);
        }
    }

    private void asignarIdSucursal(PreparedStatement sentencia, CuentaBancaria cuenta, int indice)
            throws SQLException {
        String idSucursal = cuenta.getIdSucursal();
        if (idSucursal != null && !idSucursal.isEmpty()) {
            sentencia.setString(indice, idSucursal);
        } else {
            sentencia.setNull(indice, Types.VARCHAR);
        }
    }

    private CuentaAhorros construirAhorros(ResultSet fila) throws SQLException {
        CuentaAhorros cuenta = new CuentaAhorros(
                fila.getString("numero_cuenta"),
                fila.getDouble("saldo"),
                construirClienteReferencia(fila),
                obtenerTextoOpcional(fila, "id_sucursal"),
                fila.getDouble("tasa_interes"));
        return cuenta;
    }

    private CuentaCorriente construirCorriente(ResultSet fila) throws SQLException {
        CuentaCorriente cuenta = new CuentaCorriente(
                fila.getString("numero_cuenta"),
                fila.getDouble("saldo"),
                construirClienteReferencia(fila),
                obtenerTextoOpcional(fila, "id_sucursal"),
                fila.getDouble("limite_credito"));
        return cuenta;
    }

    private CuentaEmpresarial construirEmpresarial(ResultSet fila) throws SQLException {
        CuentaEmpresarial cuenta = new CuentaEmpresarial(
                fila.getString("numero_cuenta"),
                fila.getDouble("saldo"),
                construirClienteReferencia(fila));
        cuenta.setIdSucursal(obtenerTextoOpcional(fila, "id_sucursal"));
        cuenta.setLimiteCredito(fila.getDouble("limite_credito"));
        cuenta.setRazonSocial(obtenerTextoOpcional(fila, "razon_social"));
        return cuenta;
    }

    private Cliente construirClienteReferencia(ResultSet fila) throws SQLException {
        String rfcCurp = fila.getString("rfc_curp_cliente");
        Cliente cliente = null;
        if (rfcCurp != null) {
            cliente = new Cliente();
            cliente.setRfcCurp(rfcCurp);
        }
        return cliente;
    }

    private String obtenerTextoOpcional(ResultSet fila, String columna) throws SQLException {
        String valor = fila.getString(columna);
        String resultado = valor != null ? valor : "";
        return resultado;
    }
}