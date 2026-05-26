package uv.lis.modelo;

import java.time.LocalDate;

public class EjecutivoCuenta extends Empleado {

    private static final long serialVersionUID = 1L;

    private int numeroClientesAsignados;
    private EspecializacionEjecutivo especializacion;

    public EjecutivoCuenta(String idEmpleado,
                           String nombreCompleto,
                           String direccion,
                           LocalDate fechaNacimiento,
                           Genero genero,
                           double salario,
                           String nombreUsuario,
                           String contrasenia,
                           int numeroClientesAsignados,
                           EspecializacionEjecutivo especializacion) {
        super(idEmpleado, nombreCompleto, direccion, fechaNacimiento,
              genero, salario, nombreUsuario, contrasenia);
        this.numeroClientesAsignados = numeroClientesAsignados;
        this.especializacion = especializacion;
    }

    public int getNumeroClientesAsignados() {
        return numeroClientesAsignados;
    }

    public void setNumeroClientesAsignados(int numeroClientesAsignados) {
        this.numeroClientesAsignados = numeroClientesAsignados;
    }

    public EspecializacionEjecutivo getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(EspecializacionEjecutivo especializacion) {
        this.especializacion = especializacion;
    }

    public void agregarClienteAsignado() {
        numeroClientesAsignados++;
    }

    public void removerClienteAsignado() {
        if (numeroClientesAsignados > 0) {
            numeroClientesAsignados--;
        }
    }

    public boolean atiendeEspecializacion(EspecializacionEjecutivo especializacionConsultada) {
        boolean coincide = especializacion == especializacionConsultada;
        return coincide;
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Ejecutivo de Cuenta (" + especializacion + ")";
    }
}