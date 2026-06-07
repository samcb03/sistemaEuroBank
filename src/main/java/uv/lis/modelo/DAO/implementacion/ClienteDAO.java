package uv.lis.modelo.DAO.implementacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uv.lis.modelo.Cliente;
import uv.lis.modelo.DAO.interfaces.IClienteDAO;
import uv.lis.modelo.excepcion.AccesoDatosException;
import uv.lis.modelo.excepcion.ClienteDuplicadoException;
import uv.lis.modelo.excepcion.ClienteNoEncontradoException;
import uv.lis.modelo.persistencia.ConexionBaseDatos;

public class ClienteDAO implements IClienteDAO {

    private static final String SQL_SELECCIONAR_TODOS =
            "SELECT * FROM cliente ORDER BY apellidos, nombre";
    private static final String SQL_BUSCAR_POR_RFC =
            "SELECT * FROM cliente WHERE rfc_curp = ?";
    private static final String SQL_BUSCAR_POR_CRITERIO =
            "SELECT * FROM cliente WHERE LOWER(rfc_curp) LIKE ? OR LOWER(nombre) LIKE ? "
            + "OR LOWER(apellidos) LIKE ? ORDER BY apellidos, nombre";
    private static final String SQL_CUENTAS_DEL_CLIENTE =
            "SELECT numero_cuenta FROM cuenta WHERE rfc_curp_cliente = ?";
    private static final String SQL_INSERTAR =
            "INSERT INTO cliente (rfc_curp, nombre, apellidos, nacionalidad, fecha_nacimiento, "
            + "direccion, telefono, correo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR =
            "UPDATE cliente SET nombre = ?, apellidos = ?, nacionalidad = ?, fecha_nacimiento = ?, "
            + "direccion = ?, telefono = ?, correo = ? WHERE rfc_curp = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM cliente WHERE rfc_curp = ?";

    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<Cliente>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECCIONAR_TODOS);
             ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                clientes.add(mapearDesdeFila(filas));
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener los clientes.", excepcion);
        }
        return clientes;
    }
    
    @Override
    public List<Cliente> buscarPorCriterio(String filtro) {
        List<Cliente> clientes;
        if (filtro == null || filtro.trim().isEmpty()) {
            clientes = obtenerTodos();
        } else {
            clientes = buscarConFiltro(filtro.trim().toLowerCase());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> buscarPorRfc(String rfcCurp) {
        Optional<Cliente> resultado = Optional.empty();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_RFC)) {
            sentencia.setString(1, rfcCurp);
            try (ResultSet filas = sentencia.executeQuery()) {
                if (filas.next()) {
                    resultado = Optional.of(mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo buscar el cliente.", excepcion);
        }
        return resultado;
    }

    @Override
    public void agregar(Cliente cliente) throws ClienteDuplicadoException {
        if (buscarPorRfc(cliente.getRfcCurp()).isPresent()) {
            throw new ClienteDuplicadoException(
                    "Ya existe un cliente con RFC/CURP: " + cliente.getRfcCurp());
        }
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            sentencia.setString(1, cliente.getRfcCurp());
            asignarColumnasModificables(sentencia, cliente, 2);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo registrar el cliente.", excepcion);
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws ClienteNoEncontradoException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            int indiceClave = asignarColumnasModificables(sentencia, cliente, 1);
            sentencia.setString(indiceClave, cliente.getRfcCurp());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new ClienteNoEncontradoException(
                        "No se encontro ningun cliente con RFC/CURP: " + cliente.getRfcCurp());
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo actualizar el cliente.", excepcion);
        }
    }

    @Override
    public void eliminar(String rfcCurp) throws ClienteNoEncontradoException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setString(1, rfcCurp);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new ClienteNoEncontradoException(
                        "No se encontro ningun cliente con RFC/CURP: " + rfcCurp);
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo eliminar el cliente.", excepcion);
        }
    }

    private List<Cliente> buscarConFiltro(String filtroNormalizado) {
        List<Cliente> clientes = new ArrayList<Cliente>();
        String patron = "%" + filtroNormalizado + "%";
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_CRITERIO)) {
            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
            try (ResultSet filas = sentencia.executeQuery()) {
                while (filas.next()) {
                    clientes.add(mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo buscar clientes por criterio.", excepcion);
        }
        return clientes;
    }

    private int asignarColumnasModificables(PreparedStatement sentencia, Cliente cliente, int inicio)
            throws SQLException {
        int indice = inicio;
        sentencia.setString(indice++, cliente.getNombre());
        sentencia.setString(indice++, cliente.getApellidos());
        sentencia.setString(indice++, cliente.getNacionalidad());
        sentencia.setString(indice++, cliente.getFechaNacimiento());
        sentencia.setString(indice++, cliente.getDireccion());
        sentencia.setString(indice++, cliente.getTelefono());
        sentencia.setString(indice++, cliente.getCorreo());
        return indice;
    }

    private Cliente mapearDesdeFila(ResultSet fila) throws SQLException {
        Cliente cliente = new Cliente(
                fila.getString("rfc_curp"),
                fila.getString("nombre"),
                fila.getString("apellidos"),
                fila.getString("nacionalidad"),
                fila.getString("fecha_nacimiento"),
                fila.getString("direccion"),
                fila.getString("telefono"),
                fila.getString("correo"));
        cargarNumerosCuenta(cliente);
        return cliente;
    }

    private void cargarNumerosCuenta(Cliente cliente) throws SQLException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CUENTAS_DEL_CLIENTE)) {
            sentencia.setString(1, cliente.getRfcCurp());
            try (ResultSet filas = sentencia.executeQuery()) {
                while (filas.next()) {
                    cliente.agregarCuenta(filas.getString("numero_cuenta"));
                }
            }
        }
    }
}