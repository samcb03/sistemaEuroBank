package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

public class Sucursal {

    private String numeroIdentificacion;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String nombreGerente;
    private String personaContacto;
    private List<String> numerosCuenta;
    private List<String> idsEmpleados;

    public Sucursal() {
        this.numerosCuenta = new ArrayList<String>();
        this.idsEmpleados = new ArrayList<String>();
    }

    public Sucursal(String numeroIdentificacion,
                    String nombre,
                    String direccion,
                    String telefono,
                    String correo,
                    String nombreGerente,
                    String personaContacto) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreGerente = nombreGerente;
        this.personaContacto = personaContacto;
        this.numerosCuenta = new ArrayList<String>();
        this.idsEmpleados = new ArrayList<String>();
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreGerente() {
        return nombreGerente;
    }

    public void setNombreGerente(String nombreGerente) {
        this.nombreGerente = nombreGerente;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public List<String> getNumerosCuenta() {
        return numerosCuenta;
    }

    public void setNumerosCuenta(List<String> numerosCuenta) {
        if (numerosCuenta == null) {
            this.numerosCuenta = new ArrayList<String>();
        } else {
            this.numerosCuenta = numerosCuenta;
        }
    }

    public List<String> getIdsEmpleados() {
        return idsEmpleados;
    }

    public void setIdsEmpleados(List<String> idsEmpleados) {
        if (idsEmpleados == null) {
            this.idsEmpleados = new ArrayList<String>();
        } else {
            this.idsEmpleados = idsEmpleados;
        }
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Sucursal)) {
            return false;
        }
        Sucursal otraSucursal = (Sucursal) objeto;
        return numeroIdentificacion != null
                && numeroIdentificacion.equals(otraSucursal.numeroIdentificacion);
    }

    @Override
    public int hashCode() {
        return numeroIdentificacion != null ? numeroIdentificacion.hashCode() : 0;
    }
}