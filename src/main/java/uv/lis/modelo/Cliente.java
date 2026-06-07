package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private String rfcCurp;
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private String fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correo;
    private List<String> numerosCuenta;

    public Cliente() {
        this.numerosCuenta = new ArrayList<>();
    }

    public Cliente(String rfcCurp, String nombre, String apellidos,
            String nacionalidad, String fechaNacimiento,
            String direccion, String telefono, String correo) {
        this.rfcCurp = rfcCurp;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.numerosCuenta = new ArrayList<>();
    }

    public void agregarCuenta(String numeroCuenta) {
        if (!numerosCuenta.contains(numeroCuenta)) {
            numerosCuenta.add(numeroCuenta);
        }
    }

    public void eliminarCuenta(String numeroCuenta) {
        numerosCuenta.remove(numeroCuenta);
    }

    public boolean tieneCuentasActivas() {
        return !numerosCuenta.isEmpty();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public String getRfcCurp() { return rfcCurp; }
    public void setRfcCurp(String rfcCurp) { this.rfcCurp = rfcCurp; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public List<String> getNumerosCuenta() { return numerosCuenta; }
    public void setNumerosCuenta(List<String> numerosCuenta) {
        this.numerosCuenta = numerosCuenta;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + rfcCurp + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) obj;
        return rfcCurp != null && rfcCurp.equals(other.rfcCurp);
    }

    @Override
    public int hashCode() {
        return rfcCurp != null ? rfcCurp.hashCode() : 0;
    }
}