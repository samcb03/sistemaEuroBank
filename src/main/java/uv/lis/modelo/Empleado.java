package uv.lis.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public abstract class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idEmpleado;
    private String nombreCompleto;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String genero;
    private double salario;
    private String nombreUsuario;
    private String contrasenia;

    protected Empleado(String idEmpleado,
                       String nombreCompleto,
                       String direccion,
                       LocalDate fechaNacimiento,
                       String genero,
                       double salario,
                       String nombreUsuario,
                       String contrasenia) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.salario = salario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int calcularEdad() {
        int edad = 0;
        if (fechaNacimiento != null) {
            edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        }
        return edad;
    }

    public int getEdad() {
        int edad = calcularEdad();
        return edad;
    }

    public String getDescripcionPuesto() {
        String descripcion = obtenerDescripcionPuesto();
        return descripcion;
    }

    public boolean coincideCredenciales(String usuarioIngresado, String contraseniaIngresada) {
        boolean coinciden = Objects.equals(nombreUsuario, usuarioIngresado)
                && Objects.equals(contrasenia, contraseniaIngresada);
        return coinciden;
    }

    public abstract String obtenerDescripcionPuesto();

    @Override
    public boolean equals(Object objeto) {
        boolean resultado;
        if (this == objeto) {
            resultado = true;
        } else if (!(objeto instanceof Empleado)) {
            resultado = false;
        } else {
            Empleado otroEmpleado = (Empleado) objeto;
            resultado = Objects.equals(idEmpleado, otroEmpleado.idEmpleado);
        }
        return resultado;
    }

    public abstract String obtenerRol();

    @Override
    public int hashCode() {
        return Objects.hash(idEmpleado);
    }

    @Override
    public String toString() {
        return obtenerDescripcionPuesto() + " [id=" + idEmpleado
                + ", nombre=" + nombreCompleto + "]";
    }
}