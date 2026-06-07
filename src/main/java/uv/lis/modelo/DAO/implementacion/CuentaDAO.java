package uv.lis.modelo.DAO.implementacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.DAO.interfaces.ICuentaDAO;
import uv.lis.modelo.excepcion.AccesoDatosException;
import uv.lis.modelo.excepcion.CuentaDuplicadaException;
import uv.lis.modelo.persistencia.ConexionBaseDatos;
import uv.lis.modelo.persistencia.MapeadorCuenta;


public class CuentaDAO implements ICuentaDAO {

    private static final String SQL_BASE_SELECCION =
            "SELECT cuenta.*, tipo_cuenta.nombre AS tipo FROM cuenta "
            + "JOIN tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id_tipo_cuenta ";
    private static final String SQL_SELECCIONAR_TODAS =
            SQL_BASE_SELECCION + "ORDER BY cuenta.numero_cuenta";
    private static final String SQL_BUSCAR_POR_NUMERO =
            SQL_BASE_SELECCION + "WHERE cuenta.numero_cuenta = ?";
    private static final String SQL_INSERTAR =
            "INSERT INTO cuenta (numero_cuenta, id_tipo_cuenta, saldo, rfc_curp_cliente, "
            + "id_sucursal, tasa_interes, limite_credito, razon_social) "
            + "VALUES (?, (SELECT id_tipo_cuenta FROM tipo_cuenta WHERE nombre = ?), ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR =
            "UPDATE cuenta SET id_tipo_cuenta = (SELECT id_tipo_cuenta FROM tipo_cuenta WHERE nombre = ?), "
            + "saldo = ?, rfc_curp_cliente = ?, id_sucursal = ?, tasa_interes = ?, "
            + "limite_credito = ?, razon_social = ? WHERE numero_cuenta = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM cuenta WHERE numero_cuenta = ?";

    private final MapeadorCuenta mapeadorCuenta;

    public CuentaDAO() {
        this.mapeadorCuenta = new MapeadorCuenta();
    }

    @Override
    public List<CuentaBancaria> obtenerTodas() {
        List<CuentaBancaria> cuentas = new ArrayList<CuentaBancaria>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECCIONAR_TODAS);
             ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                cuentas.add(mapeadorCuenta.mapearDesdeFila(filas));
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener las cuentas.", excepcion);
        }
        return cuentas;
    }

    @Override
    public Optional<CuentaBancaria> buscarPorNumero(String numeroCuenta) {
        Optional<CuentaBancaria> resultado = Optional.empty();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_NUMERO)) {
            sentencia.setString(1, numeroCuenta);
            try (ResultSet filas = sentencia.executeQuery()) {
                if (filas.next()) {
                    resultado = Optional.of(mapeadorCuenta.mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo buscar la cuenta.", excepcion);
        }
        return resultado;
    }

    @Override
    public boolean existe(String numeroCuenta) {
        boolean encontrada = buscarPorNumero(numeroCuenta).isPresent();
        return encontrada;
    }

    @Override
    public void agregar(CuentaBancaria cuenta) throws CuentaDuplicadaException {
        if (existe(cuenta.getNumeroCuenta())) {
            throw new CuentaDuplicadaException(
                    "Ya existe una cuenta con el numero: " + cuenta.getNumeroCuenta());
        }
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            mapeadorCuenta.asignarParametrosInsercion(sentencia, cuenta);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo abrir la cuenta.", excepcion);
        }
    }

    @Override
    public void actualizar(CuentaBancaria cuenta) {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            mapeadorCuenta.asignarParametrosActualizacion(sentencia, cuenta);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo actualizar la cuenta.", excepcion);
        }
    }

    @Override
    public void eliminar(String numeroCuenta) {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setString(1, numeroCuenta);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo eliminar la cuenta.", excepcion);
        }
    }
}