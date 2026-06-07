package uv.lis.modelo.DAO.implementacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import uv.lis.modelo.Transaccion;
import uv.lis.modelo.DAO.interfaces.ITransaccionDAO;
import uv.lis.modelo.excepcion.AccesoDatosException;
import uv.lis.modelo.persistencia.ConexionBaseDatos;

public class TransaccionDAO implements ITransaccionDAO {

    private static final String SQL_SELECCIONAR_TODAS =
            "SELECT * FROM transaccion ORDER BY fecha_hora";
    private static final String SQL_BUSCAR_POR_CUENTA =
            "SELECT * FROM transaccion WHERE cuenta_origen = ? OR cuenta_destino = ? "
            + "ORDER BY fecha_hora";
    private static final String SQL_INSERTAR =
            "INSERT INTO transaccion (id_transaccion, tipo, monto, fecha_hora, cuenta_origen, "
            + "cuenta_destino, id_sucursal) VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<Transaccion> obtenerTodas() {
        List<Transaccion> transacciones = new ArrayList<Transaccion>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECCIONAR_TODAS);
             ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                transacciones.add(mapearDesdeFila(filas));
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener las transacciones.", excepcion);
        }
        return transacciones;
    }

    @Override
    public List<Transaccion> obtenerPorCuenta(String numeroCuenta) {
        List<Transaccion> transacciones = new ArrayList<Transaccion>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_CUENTA)) {
            sentencia.setString(1, numeroCuenta);
            sentencia.setString(2, numeroCuenta);
            try (ResultSet filas = sentencia.executeQuery()) {
                while (filas.next()) {
                    transacciones.add(mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener las transacciones de la cuenta.", excepcion);
        }
        return transacciones;
    }

    @Override
    public void agregar(Transaccion transaccion) {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            sentencia.setString(1, transaccion.getIdTransaccion());
            sentencia.setString(2, transaccion.getTipo());
            sentencia.setDouble(3, transaccion.getMonto());
            sentencia.setString(4, transaccion.getFechaHora());
            asignarTextoOpcional(sentencia, 5, transaccion.getCuentaOrigen());
            asignarTextoOpcional(sentencia, 6, transaccion.getCuentaDestino());
            asignarTextoOpcional(sentencia, 7, transaccion.getIdSucursal());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo registrar la transaccion.", excepcion);
        }
    }

    private void asignarTextoOpcional(PreparedStatement sentencia, int indice, String valor)
            throws SQLException {
        if (valor != null && !valor.isEmpty()) {
            sentencia.setString(indice, valor);
        } else {
            sentencia.setNull(indice, Types.VARCHAR);
        }
    }

    private Transaccion mapearDesdeFila(ResultSet fila) throws SQLException {
        Transaccion transaccion = new Transaccion();
        transaccion.setIdTransaccion(fila.getString("id_transaccion"));
        transaccion.setTipo(fila.getString("tipo"));
        transaccion.setMonto(fila.getDouble("monto"));
        transaccion.setFechaHora(fila.getString("fecha_hora"));
        transaccion.setCuentaOrigen(fila.getString("cuenta_origen"));
        transaccion.setCuentaDestino(fila.getString("cuenta_destino"));
        transaccion.setIdSucursal(fila.getString("id_sucursal"));
        return transaccion;
    }
}