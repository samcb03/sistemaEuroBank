package uv.lis.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Cajero extends Empleado {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime horaInicioTurno;
    private LocalTime horaFinTurno;
    private int numeroVentanilla;

    public Cajero(String idEmpleado,
                  String nombreCompleto,
                  String direccion,
                  LocalDate fechaNacimiento,
                  String genero,
                  double salario,
                  String nombreUsuario,
                  String contrasenia,
                  LocalTime horaInicioTurno,
                  LocalTime horaFinTurno,
                  int numeroVentanilla) {
        super(idEmpleado, nombreCompleto, direccion, fechaNacimiento,
              genero, salario, nombreUsuario, contrasenia);
        this.horaInicioTurno = horaInicioTurno;
        this.horaFinTurno = horaFinTurno;
        this.numeroVentanilla = numeroVentanilla;
    }

    public LocalTime getHoraInicioTurno() {
        return horaInicioTurno;
    }

    public void setHoraInicioTurno(LocalTime horaInicioTurno) {
        this.horaInicioTurno = horaInicioTurno;
    }

    public LocalTime getHoraFinTurno() {
        return horaFinTurno;
    }

    public void setHoraFinTurno(LocalTime horaFinTurno) {
        this.horaFinTurno = horaFinTurno;
    }

    public int getNumeroVentanilla() {
        return numeroVentanilla;
    }

    public void setNumeroVentanilla(int numeroVentanilla) {
        this.numeroVentanilla = numeroVentanilla;
    }

    public String obtenerHorarioFormateado() {
        String horario = horaInicioTurno.format(FORMATO_HORA)
                + " - " + horaFinTurno.format(FORMATO_HORA);
        return horario;
    }

    public boolean estaEnHorarioLaboral(LocalTime horaConsultada) {
        boolean dentroDelTurno = !horaConsultada.isBefore(horaInicioTurno)
                && !horaConsultada.isAfter(horaFinTurno);
        return dentroDelTurno;
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Cajero";
    }

    @Override
    public String obtenerRol() {
        return CatalogoRol.CAJERO;
    }

    @Override
    public PermisosEmpleado obtenerPermisos() {
        PermisosEmpleado permisos = PermisosEmpleado.constructor()
                .permitirGestionarCuentas()
                .permitirRegistrarTransacciones()
                .permitirGestionarClientes()
                .construir();
        return permisos;
    }
}