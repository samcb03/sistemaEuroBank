package uv.lis.modelo;

import java.time.LocalDate;

public class Gerente extends Empleado {

    private static final long serialVersionUID = 1L;

    private NivelAccesoGerente nivelAcceso;
    private int aniosExperiencia;

    public Gerente(String idEmpleado,
                   String nombreCompleto,
                   String direccion,
                   LocalDate fechaNacimiento,
                   Genero genero,
                   double salario,
                   String nombreUsuario,
                   String contrasenia,
                   NivelAccesoGerente nivelAcceso,
                   int aniosExperiencia) {
        super(idEmpleado, nombreCompleto, direccion, fechaNacimiento,
              genero, salario, nombreUsuario, contrasenia);
        this.nivelAcceso = nivelAcceso;
        this.aniosExperiencia = aniosExperiencia;
    }

    public NivelAccesoGerente getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(NivelAccesoGerente nivelAcceso) {
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
        boolean accesoNacional = nivelAcceso == NivelAccesoGerente.NACIONAL;
        return accesoNacional;
    }

    public boolean puedeAdministrarRegion() {
        boolean puedeRegion = nivelAcceso == NivelAccesoGerente.REGIONAL
                || nivelAcceso == NivelAccesoGerente.NACIONAL;
        return puedeRegion;
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Gerente (" + nivelAcceso + ")";
    }

    @Override
    public Rol obtenerRol() {
        return Rol.GERENTE;
    }
}