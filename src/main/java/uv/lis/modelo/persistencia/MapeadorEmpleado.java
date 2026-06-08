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
import uv.lis.modelo.CatalogoRol;
import uv.lis.modelo.Gerente;

public class MapeadorEmpleado {

    public Empleado mapearDesdeFila(ResultSet fila) throws SQLException {
        String rol = fila.getString("rol");
        Empleado empleado;
        if (CatalogoRol.ADMINISTRADOR.equals(rol)) {
            empleado = construirAdministrador(fila);
        } else if (CatalogoRol.GERENTE.equals(rol)) {
            empleado = construirGerente(fila);
        } else if (CatalogoRol.CAJERO.equals(rol)) {
            empleado = construirCajero(fila);
        } else {
            empleado = construirEjecutivo(fila);
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
        sentencia.setString(indice++, empleado.getGenero());
        sentencia.setDouble(indice++, empleado.getSalario());
        sentencia.setString(indice++, empleado.getNombreUsuario());
        sentencia.setString(indice++, empleado.getContrasenia());
        sentencia.setString(indice++, empleado.obtenerRol());
        indice = asignarColumnasEspecificas(sentencia, empleado, indice);
        sentencia.setNull(indice++, Types.VARCHAR);
        return indice;
    }

    private int asignarColumnasEspecificas(PreparedStatement sentencia, Empleado empleado, int inicio)
            throws SQLException {
        String rol = empleado.obtenerRol();
        if (CatalogoRol.CAJERO.equals(rol)) {
            asignarEspecificasCajero(sentencia, (Cajero) empleado, inicio);
        } else if (CatalogoRol.EJECUTIVO.equals(rol)) {
            asignarEspecificasEjecutivo(sentencia, (EjecutivoCuenta) empleado, inicio);
        } else if (CatalogoRol.GERENTE.equals(rol)) {
            asignarEspecificasGerente(sentencia, (Gerente) empleado, inicio);
        } else {
            asignarEspecificasNulas(sentencia, inicio);
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
        sentencia.setString(inicio + 4, ejecutivo.getEspecializacion());
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
        sentencia.setString(inicio + 5, gerente.getNivelAcceso());
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
                fila.getString("genero"),
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
                fila.getString("genero"),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"),
                fila.getString("nivel_acceso"),
                fila.getInt("anios_experiencia"));
        return gerente;
    }

    private Cajero construirCajero(ResultSet fila) throws SQLException {
        Cajero cajero = new Cajero(
                fila.getString("id_empleado"),
                fila.getString("nombre_completo"),
                fila.getString("direccion"),
                leerFechaNacimiento(fila),
                fila.getString("genero"),
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
                fila.getString("genero"),
                fila.getDouble("salario"),
                fila.getString("nombre_usuario"),
                fila.getString("contrasenia"),
                fila.getInt("numero_clientes_asignados"),
                fila.getString("especializacion"));
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