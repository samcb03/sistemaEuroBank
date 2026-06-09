package uv.lis.modelo;

import java.util.Optional;

import uv.lis.modelo.DAO.implementacion.EmpleadoDAO;

public class ServicioAutenticacion {

    private final EmpleadoDAO repositorioEmpleados;

    public ServicioAutenticacion(EmpleadoDAO repositorioEmpleados) {
        this.repositorioEmpleados = repositorioEmpleados;
    }

    public ResultadoAutenticacion autenticar(String nombreUsuario, String contrasenia) {
        ResultadoAutenticacion resultado;
        Optional<Empleado> empleadoEncontrado = repositorioEmpleados.buscarPorNombreUsuario(nombreUsuario);
        if (!empleadoEncontrado.isPresent()) {
            resultado = ResultadoAutenticacion.crearFallo("El usuario ingresado no existe.");
        } else {
            resultado = verificarCredenciales(empleadoEncontrado.get(), nombreUsuario, contrasenia);
        }
        return resultado;
    }

    private ResultadoAutenticacion verificarCredenciales(Empleado empleado, String nombreUsuario, String contrasenia) {
        ResultadoAutenticacion resultado;
        if (empleado.coincideCredenciales(nombreUsuario, contrasenia)) {
            resultado = ResultadoAutenticacion.crearExito(empleado);
        } else {
            resultado = ResultadoAutenticacion.crearFallo("La contrasena es incorrecta.");
        }
        return resultado;
    }
}