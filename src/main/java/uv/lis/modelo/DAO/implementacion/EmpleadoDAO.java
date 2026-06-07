package uv.lis.modelo.DAO.implementacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uv.lis.modelo.Empleado;
import uv.lis.modelo.DAO.interfaces.IEmpleadoDAO;
import uv.lis.modelo.excepcion.AccesoDatosException;
import uv.lis.modelo.excepcion.EmpleadoDuplicadoException;
import uv.lis.modelo.excepcion.EmpleadoNoEncontradoException;
import uv.lis.modelo.persistencia.ConexionBaseDatos;
import uv.lis.modelo.persistencia.MapeadorEmpleado;

public class EmpleadoDAO implements IEmpleadoDAO {

    private static final String SQL_BASE_SELECCION =
            "SELECT empleado.*, genero.nombre AS genero, rol.nombre AS rol, "
            + "especializacion_ejecutivo.nombre AS especializacion, "
            + "nivel_acceso_gerente.nombre AS nivel_acceso "
            + "FROM empleado "
            + "JOIN genero ON empleado.id_genero = genero.id_genero "
            + "JOIN rol ON empleado.id_rol = rol.id_rol "
            + "LEFT JOIN especializacion_ejecutivo "
            + "ON empleado.id_especializacion = especializacion_ejecutivo.id_especializacion "
            + "LEFT JOIN nivel_acceso_gerente "
            + "ON empleado.id_nivel_acceso = nivel_acceso_gerente.id_nivel_acceso ";
    private static final String SQL_SELECCIONAR_TODOS =
            SQL_BASE_SELECCION + "ORDER BY empleado.id_empleado";
    private static final String SQL_BUSCAR_POR_ID =
            SQL_BASE_SELECCION + "WHERE empleado.id_empleado = ?";
    private static final String SQL_BUSCAR_POR_USUARIO =
            SQL_BASE_SELECCION + "WHERE empleado.nombre_usuario = ?";
    private static final String SQL_INSERTAR =
            "INSERT INTO empleado (id_empleado, nombre_completo, direccion, fecha_nacimiento, "
            + "id_genero, salario, nombre_usuario, contrasenia, id_rol, hora_inicio_turno, "
            + "hora_fin_turno, numero_ventanilla, numero_clientes_asignados, id_especializacion, "
            + "id_nivel_acceso, anios_experiencia, id_sucursal) "
            + "VALUES (?, ?, ?, ?, (SELECT id_genero FROM genero WHERE nombre = ?), ?, ?, ?, "
            + "(SELECT id_rol FROM rol WHERE nombre = ?), ?, ?, ?, ?, "
            + "(SELECT id_especializacion FROM especializacion_ejecutivo WHERE nombre = ?), "
            + "(SELECT id_nivel_acceso FROM nivel_acceso_gerente WHERE nombre = ?), ?, ?)";
    private static final String SQL_ACTUALIZAR =
            "UPDATE empleado SET nombre_completo = ?, direccion = ?, fecha_nacimiento = ?, "
            + "id_genero = (SELECT id_genero FROM genero WHERE nombre = ?), salario = ?, "
            + "nombre_usuario = ?, contrasenia = ?, "
            + "id_rol = (SELECT id_rol FROM rol WHERE nombre = ?), "
            + "hora_inicio_turno = ?, hora_fin_turno = ?, numero_ventanilla = ?, "
            + "numero_clientes_asignados = ?, "
            + "id_especializacion = (SELECT id_especializacion FROM especializacion_ejecutivo WHERE nombre = ?), "
            + "id_nivel_acceso = (SELECT id_nivel_acceso FROM nivel_acceso_gerente WHERE nombre = ?), "
            + "anios_experiencia = ?, id_sucursal = ? WHERE id_empleado = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM empleado WHERE id_empleado = ?";

    private final MapeadorEmpleado mapeadorEmpleado;

    public EmpleadoDAO() {
        this.mapeadorEmpleado = new MapeadorEmpleado();
    }

    @Override
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<Empleado>();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_SELECCIONAR_TODOS);
             ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                empleados.add(mapeadorEmpleado.mapearDesdeFila(filas));
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudieron obtener los empleados.", excepcion);
        }
        return empleados;
    }

    @Override
    public Optional<Empleado> buscarPorId(String idEmpleado) {
        Optional<Empleado> resultado = buscarPorColumna(SQL_BUSCAR_POR_ID, idEmpleado);
        return resultado;
    }

    @Override
    public Optional<Empleado> buscarPorNombreUsuario(String nombreUsuario) {
        Optional<Empleado> resultado = buscarPorColumna(SQL_BUSCAR_POR_USUARIO, nombreUsuario);
        return resultado;
    }

    @Override
    public void agregar(Empleado empleado) throws EmpleadoDuplicadoException {
        validarNoDuplicado(empleado);
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            mapeadorEmpleado.asignarParametrosInsercion(sentencia, empleado);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo agregar el empleado.", excepcion);
        }
    }

    @Override
    public void actualizar(Empleado empleado) throws EmpleadoNoEncontradoException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            mapeadorEmpleado.asignarParametrosActualizacion(sentencia, empleado);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new EmpleadoNoEncontradoException(
                        "No se encontro el empleado con ID " + empleado.getIdEmpleado() + ".");
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo actualizar el empleado.", excepcion);
        }
    }

    @Override
    public void eliminar(String idEmpleado) throws EmpleadoNoEncontradoException {
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setString(1, idEmpleado);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                throw new EmpleadoNoEncontradoException(
                        "No se encontro el empleado con ID " + idEmpleado + ".");
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo eliminar el empleado.", excepcion);
        }
    }

    private Optional<Empleado> buscarPorColumna(String sentenciaSql, String valor) {
        Optional<Empleado> resultado = Optional.empty();
        try (Connection conexion = ConexionBaseDatos.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
            sentencia.setString(1, valor);
            try (ResultSet filas = sentencia.executeQuery()) {
                if (filas.next()) {
                    resultado = Optional.of(mapeadorEmpleado.mapearDesdeFila(filas));
                }
            }
        } catch (SQLException excepcion) {
            throw new AccesoDatosException("No se pudo buscar el empleado.", excepcion);
        }
        return resultado;
    }

    private void validarNoDuplicado(Empleado empleado) throws EmpleadoDuplicadoException {
        if (buscarPorId(empleado.getIdEmpleado()).isPresent()) {
            throw new EmpleadoDuplicadoException(
                    "Ya existe un empleado con el ID " + empleado.getIdEmpleado() + ".");
        }
        if (buscarPorNombreUsuario(empleado.getNombreUsuario()).isPresent()) {
            throw new EmpleadoDuplicadoException(
                    "Ya existe un empleado con el usuario " + empleado.getNombreUsuario() + ".");
        }
    }
}