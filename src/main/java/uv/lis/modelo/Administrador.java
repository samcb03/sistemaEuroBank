package uv.lis.modelo;

import java.time.LocalDate;

public class Administrador extends Empleado {

    private static final long serialVersionUID = 1L;

    public Administrador(String idEmpleado,
                         String nombreCompleto,
                         String direccion,
                         LocalDate fechaNacimiento,
                         Genero genero,
                         double salario,
                         String nombreUsuario,
                         String contrasenia) {
        super(idEmpleado, nombreCompleto, direccion, fechaNacimiento,
              genero, salario, nombreUsuario, contrasenia);
    }

    @Override
    public String obtenerDescripcionPuesto() {
        return "Administrador";
    }

    @Override
    public Rol obtenerRol() {
        return Rol.ADMINISTRADOR;
    }
}