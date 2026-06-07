package uv.lis.modelo.persistencia;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import uv.lis.modelo.Administrador;
import uv.lis.modelo.Cajero;
import uv.lis.modelo.EjecutivoCuenta;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.EspecializacionEjecutivo;
import uv.lis.modelo.Genero;
import uv.lis.modelo.Gerente;
import uv.lis.modelo.NivelAccesoGerente;
import uv.lis.modelo.Rol;

/**
 * Traduce entre filas de la tabla "empleado" y la jerarquia Empleado.
 * El despacho por subtipo se realiza con un switch sobre el rol
 * (lectura) o sobre obtenerRol() (escritura), evitando instanceof.
 */
public class MapeadorEmpleado {

    public Empleado mapearDesdeFila(ResultSet fila) throws SQLException {
        Rol rol = Rol.valueOf(fila.getString("rol"));
        Empleado empleado;
        switch (rol) {
            case ADMINISTRADOR:
                empleado = construirAdministrador(fila);
                break;
            case GERENTE:
                empleado = construirGerente(fila);
                break;
            case CAJERO:
                empleado = construirCajero(fila);
                break;
            default:
                empleado = construirEjecutivo(fila);
                break;
        }
        return empleado;
    }

    public void asignarParametrosInsercion(PreparedStatement sentencia, Empleado empleado)
            throws SQLException {
        sentencia.setString(1, empleado.getIdEmpleado());
        asignarColumnasModificables(sentencia, empleado, 2);
    }

    public void asignarParametrosActualizacion(PreparedStatement sentencia, Empleado empleado)
            throws SQLException {
        int indiceClave = asignarColumnasModificables(sentencia, empleado, 1);
        sentencia.setString(indiceClave, empleado.getIdEmpleado());
    }

    private int asignarColumnasModificables(PreparedStatement sentencia, Empleado empleado, int inicio)
            throws SQLException {
        int indice = inicio;
        sentencia.setString(indice++, empleado.getNombreCompleto());
        sentencia.setString(indice++, empleado.getDireccion());
        sentencia.setDate(indice++, Date.valueOf(empleado.getFechaNacimiento()));
        sentencia.setString(indice++, empleado.getGenero().name());
        sentencia.setDouble(indice++, empleado.getSalario());
        sentencia.setString(indice++, empleado.getNombreUsuario());
        sentencia.setString(indice++, empleado.getContrasenia());
        sentencia.setString(indice++, empleado.obtenerRol().name());
        indice = asignarColumnasEspecificas(sentencia, empleado, indice);
        sentencia.setNull(indice++, Types.VARCHAR);
        return indice;
    }

    private int asignarColumnasEspecificas(PreparedStatement sentencia, Empleado empleado, int inicio)
            throws SQLException {
        switch (empleado.obtenerRol()) {
            case CAJERO:
                asignarEspecificasCajero(sentencia, (Cajero) empleado, inicio);
                break;
            case EJECUTIVO:
                asignarEspecificasEjecutivo(sentencia, (EjecutivoCuenta) empleado, inicio);
                break;
            case GERENTE:
                asignarEspecificasGerente(sentencia, (Gerente) empleado, inicio);
                break;
            default:
                asignarEspecificasNulas(sentencia, inicio);
                break;
        }
        int indiceSiguiente = inicio + 7;
        return indiceSiguiente;
    }

    private void asignarEspecificasCajero(PreparedStatement sentencia, Cajero cajero, int inicio)
            throws SQLException {
        sentencia.setTime(inicio, Time.valueOf(cajero.getHoraInicioTurno()));
        sentencia.setTime(inicio + 1, Time.valueOf(cajero.getHoraFinTurno()));
        sentencia.setInt(inicio + 2, cajero.getNumeroVentanilla());
        sentencia.setNull(inicio + 3, Types.INTEGER);
        sentencia.setNull(inicio + 4, Types.VARCHAR);
        sentencia.setNull(inicio + 5, Types.VARCHAR);
        sentencia.setNull(inicio + 6, Types.INTEGER);
    }

    private void asignarEspecificasEjecutivo(PreparedStatement sentencia, EjecutivoCuenta ejecutivo, int inicio)
            throws SQLException {
        sentencia.setNull(inicio, Types.TIME);
        sentencia.setNull(inicio + 1, Types.TIME);
        sentencia.setNull(inicio + 2, Types.INTEGER);
        sentencia.setInt(inicio + 3, ejecutivo.getNumeroClientesAsignados());
        sentencia.setString(inicio + 4, ejecutivo.getEspecializacion().name());
        sentencia.setNull(inicio + 5, Types.VARCHAR);
        sentencia.setNull(inicio + 6, Types.INTEGER);
    }

    private void asignarEspecificasGerente(PreparedStatement sentencia, Gerente gerente, int inicio)
            throws SQLException {
        sentencia.setNull(inicio, Types.TIME);
        sentencia.setNull(inicio + 1, Types.TIME);
        sentencia.setNull(inicio + 2, Types.INTEGER);
        sentencia.setNull(inicio + 3, Types.INTEGER);
        sentencia.setNull(inicio + 4, Types.VARCHAR);
        sentencia.setString(inicio + 5, gerente.getNivelAcceso().name());
        sentencia.setInt(inicio + 6, gerente.getAniosExperiencia());
    }

    private void asignarEspecificasNulas(PreparedStatement sentencia, int inicio) throws SQLException {
        sentencia.setNull(inicio, Types.TIME);
        sentencia.setNull(inicio + 1, Types.TIME);
        sentencia.setNull(inicio + 2, Types.INTEGER);
        sentencia.setNull(inicio + 3, Types.INTEGER);
        sentencia.setNull(inicio + 4, Types.VARCHAR);
        sentencia.setNull(inicio + 5, Types.VARCHAR);
        sentencia.setNull(inicio + 6, Types.INTEGER);
    }

    private Administrador construirAdministrador(ResultSet fila) throws SQLException {
        Administrador administrador = new Administrador(
                fila.getString("id_empleado"),
                fila.getString("nombre_completo"),
                fila.getString("direccion"),
                leerFechaNacimiento(fila),
                Genero.valueOf(fila.getString("genero")),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"));
        return administrador;
    }

    private Gerente construirGerente(ResultSet fila) throws SQLException {
        Gerente gerente = new Gerente(
                fila.getString("id_empleado"),
                fila.getString("nombre_completo"),
                fila.getString("direccion"),
                leerFechaNacimiento(fila),
                Genero.valueOf(fila.getString("genero")),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"),
                NivelAccesoGerente.valueOf(fila.getString("nivel_acceso")),
                fila.getInt("anios_experiencia"));
        return gerente;
    }

    private Cajero construirCajero(ResultSet fila) throws SQLException {
        Cajero cajero = new Cajero(
                fila.getString("id_empleado"),
                fila.getString("nombre_completo"),
                fila.getString("direccion"),
                leerFechaNacimiento(fila),
                Genero.valueOf(fila.getString("genero")),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"),
                leerHora(fila, "hora_inicio_turno"),
                leerHora(fila, "hora_fin_turno"),
                fila.getInt("numero_ventanilla"));
        return cajero;
    }

    private EjecutivoCuenta construirEjecutivo(ResultSet fila) throws SQLException {
        EjecutivoCuenta ejecutivo = new EjecutivoCuenta(
                fila.getString("id_empleado"),
                fila.getString("nombre_completo"),
                fila.getString("direccion"),
                leerFechaNacimiento(fila),
                Genero.valueOf(fila.getString("genero")),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"),
                fila.getInt("numero_clientes_asignados"),
                EspecializacionEjecutivo.valueOf(fila.getString("especializacion")));
        return ejecutivo;
    }

    private LocalDate leerFechaNacimiento(ResultSet fila) throws SQLException {
        Date fecha = fila.getDate("fecha_nacimiento");
        LocalDate fechaNacimiento = fecha != null ? fecha.toLocalDate() : null;
        return fechaNacimiento;
    }

    private LocalTime leerHora(ResultSet fila, String columna) throws SQLException {
        Time hora = fila.getTime(columna);
        LocalTime valor = hora != null ? hora.toLocalTime() : null;
        return valor;
    }
}