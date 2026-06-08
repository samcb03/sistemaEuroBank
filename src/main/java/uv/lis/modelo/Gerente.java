package uv.lis.modelo;

import java.time.LocalDate;

public class Gerente extends Empleado {

    private static final long serialVersionUID = 1L;

    private String nivelAcceso;
    private int aniosExperiencia;

    public Gerente(String idEmpleado,
                   String nombreCompleto,
                   String direccion,
                   LocalDate fechaNacimiento,
                   String genero,
                   double salario,
                   String nombreUsuario,
                   String contrasenia,
                   String nivelAcceso,
                   int aniosExperiencia) {
        super(idEmpleado, nombreCompleto, direccion, fechaNacimiento,
              genero, salario, nombreUsuario, contrasenia);
        this.nivelAcceso = nivelAcceso;
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public void incrementarAnioExperiencia() {
        aniosExperiencia++;
    }

    public boolean tieneAccesoNacional() {
        boolean accesoNacional = CatalogoNivelAccesoGerente.NACIONAL.equals(nivelAcceso);
        return accesoNacional;
    }

    public boolean puedeAdministrarRegion() {
        boolean puedeRegion = CatalogoNivelAccesoGerente.REGIONAL.equals(nivelAcceso)
                || CatalogoNivelAccesoGerente.NACIONAL.equals(nivelAcceso);
        return puedeRegion;
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Gerente (" + nivelAcceso + ")";
    }

    @Override
    public String obtenerRol() {
        return CatalogoRol.GERENTE;
    }

    @Override
    public PermisosEmpleado obtenerPermisos() {
        PermisosEmpleado permisos = PermisosEmpleado.constructor()
                .permitirAdministrarEmpleados()
                .permitirGestionarSucursales()
                .permitirGestionarCuentas()
                .permitirRegistrarTransacciones()
                .permitirGestionarClientes()
                .construir();
        return permisos;
    }
}