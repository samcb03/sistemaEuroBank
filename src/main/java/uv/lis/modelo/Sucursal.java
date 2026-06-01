package uv.lis.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idSucursal;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String nombreGerente;
    private String personaContacto;

    public Sucursal(String idSucursal,
                    String nombre,
                    String direccion,
                    String telefono,
                    String correo,
                    String nombreGerente,
                    String personaContacto) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreGerente = nombreGerente;
        this.personaContacto = personaContacto;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
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

    public String obtenerResumen() {
        String resumen = nombre + " (" + idSucursal + ") - " + direccion;
        return resumen;
    }

    @Override
    public boolean equals(Object objeto) {
        boolean resultado;
        if (this == objeto) {
            resultado = true;
        } else if (!(objeto instanceof Sucursal)) {
            resultado = false;
        } else {
            Sucursal otraSucursal = (Sucursal) objeto;
            resultado = Objects.equals(idSucursal, otraSucursal.idSucursal);
        }
        return resultado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSucursal);
    }

    @Override
    public String toString() {
        return "Sucursal [id=" + idSucursal + ", nombre=" + nombre + "]";
    }
}