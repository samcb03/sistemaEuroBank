package uv.lis.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uv.lis.modelo.excepcion.AccesoDatosException;
import uv.lis.modelo.excepcion.SucursalDuplicadaException;
import uv.lis.modelo.excepcion.SucursalNoEncontradaException;
import uv.lis.modelo.persistencia.ConexionBaseDatos;

public class SucursalDAO {

    private static final String SQL_SELECCIONAR_TODAS =
            "SELECT * FROM sucursal ORDER BY numero_identificacion";
    private static final String SQL_BUSCAR_POR_NUMERO =
            "SELECT * FROM sucursal WHERE numero_identificacion = ?";
    private static final String SQL_INSERTAR =
            "INSERT INTO sucursal (numero_identificacion, nombre, direccion, telefono, correo, "
            + "nombre_gerente, persona_contacto) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR =
            "UPDATE sucursal SET nombre = ?, direccion = ?, telefono = ?, correo = ?, "
            + "nombre_gerente = ?, persona_contacto = ? WHERE numero_identificacion = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM sucursal WHERE numero_identificacion = ?";

    public List<Sucursal> obtenerTodas() {
        List<Sucursal> sucursales = new ArrayList<Sucursal>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECCIONAR_TODAS);
             ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                sucursales.add(mapearDesdeFila(filas));
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener las sucursales.", excepcion);
        }
        return sucursales;
    }

    public Optional<Sucursal> buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        Optional<Sucursal> resultado = Optional.empty();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_NUMERO)) {
            sentencia.setString(1, numeroIdentificacion);
            try (ResultSet filas = sentencia.executeQuery()) {
                if (filas.next()) {
                    resultado = Optional.of(mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo buscar la sucursal.", excepcion);
        }
        return resultado;
    }

    public void agregar(Sucursal sucursal) throws SucursalDuplicadaException {
        validarSucursal(sucursal);
        if (buscarPorNumeroIdentificacion(sucursal.getNumeroIdentificacion()).isPresent()) {
            throw new SucursalDuplicadaException(
                    "Ya existe una sucursal con el numero " + sucursal.getNumeroIdentificacion() + ".");
        }
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            sentencia.setString(1, sucursal.getNumeroIdentificacion());
            asignarColumnasModificables(sentencia, sucursal, 2);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo agregar la sucursal.", excepcion);
        }
    }

    public void actualizar(Sucursal sucursal) throws SucursalNoEncontradaException {
        validarSucursal(sucursal);
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            int indiceClave = asignarColumnasModificables(sentencia, sucursal, 1);
            sentencia.setString(indiceClave, sucursal.getNumeroIdentificacion());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SucursalNoEncontradaException(
                        "No se encontro la sucursal " + sucursal.getNumeroIdentificacion() + ".");
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo actualizar la sucursal.", excepcion);
        }
    }

    public void eliminar(String numeroIdentificacion) throws SucursalNoEncontradaException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setString(1, numeroIdentificacion);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SucursalNoEncontradaException(
                        "No se encontro la sucursal " + numeroIdentificacion + ".");
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo eliminar la sucursal.", excepcion);
        }
    }

    private int asignarColumnasModificables(PreparedStatement sentencia, Sucursal sucursal, int inicio)
            throws SQLException {
        int indice = inicio;
        sentencia.setString(indice++, sucursal.getNombre());
        sentencia.setString(indice++, sucursal.getDireccion());
        sentencia.setString(indice++, sucursal.getTelefono());
        sentencia.setString(indice++, sucursal.getCorreo());
        sentencia.setString(indice++, sucursal.getNombreGerente());
        sentencia.setString(indice++, sucursal.getPersonaContacto());
        return indice;
    }

    private Sucursal mapearDesdeFila(ResultSet fila) throws SQLException {
        Sucursal sucursal = new Sucursal(
                fila.getString("numero_identificacion"),
                fila.getString("nombre"),
                fila.getString("direccion"),
                fila.getString("telefono"),
                fila.getString("correo"),
                fila.getString("nombre_gerente"),
                fila.getString("persona_contacto"));
        return sucursal;
    }

    private void validarSucursal(Sucursal sucursal) {
        if (estaVacio(sucursal.getNumeroIdentificacion())
                || estaVacio(sucursal.getNombre())
                || estaVacio(sucursal.getDireccion())
                || estaVacio(sucursal.getTelefono())
                || estaVacio(sucursal.getCorreo())
                || estaVacio(sucursal.getNombreGerente())
                || estaVacio(sucursal.getPersonaContacto())) {
            throw new IllegalArgumentException("Todos los campos de la sucursal son obligatorios.");
        }
        if (!sucursal.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo de la sucursal no tiene un formato valido.");
        }
    }

    private boolean estaVacio(String texto) {
        boolean vacio = texto == null || texto.trim().isEmpty();
        return vacio;
    }
}