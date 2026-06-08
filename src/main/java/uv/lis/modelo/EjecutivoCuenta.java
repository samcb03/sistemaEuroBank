package uv.lis.modelo;

import java.time.LocalDate;

public class EjecutivoCuenta extends Empleado {

    private static final long serialVersionUID = 1L;

    private int numeroClientesAsignados;
    private String especializacion;

    public EjecutivoCuenta(String idEmpleado,
                           String nombreCompleto,
                           String direccion,
                           LocalDate fechaNacimiento,
                           String genero,
                           double salario,
                           String nombreUsuario,
                           String contrasenia,
                           int numeroClientesAsignados,
                           String especializacion) {
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

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
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

    public boolean atiendeEspecializacion(String especializacionConsultada) {
        boolean coincide = especializacion != null && especializacion.equals(especializacionConsultada);
        return coincide;
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Ejecutivo de Cuenta (" + especializacion + ")";
    }

    @Override
    public String obtenerRol() {
        return CatalogoRol.EJECUTIVO;
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