package uv.lis.modelo;

import java.util.Optional;

public class ResultadoAutenticacion {

    private final boolean autenticado;
    private final String mensaje;
    private final Empleado empleadoAutenticado;

    private ResultadoAutenticacion(boolean autenticado, String mensaje, Empleado empleadoAutenticado) {
        this.autenticado = autenticado;
        this.mensaje = mensaje;
        this.empleadoAutenticado = empleadoAutenticado;
    }

    public static ResultadoAutenticacion crearExito(Empleado empleado) {
        return new ResultadoAutenticacion(true, "Acceso concedido.", empleado);
    }

    public static ResultadoAutenticacion crearFallo(String mensaje) {
        return new ResultadoAutenticacion(false, mensaje, null);
    }

    public boolean fueAutenticado() {
        return autenticado;
    }

    public String obtenerMensaje() {
        return mensaje;
    }

    public Optional<Empleado> obtenerEmpleadoAutenticado() {
        return Optional.ofNullable(empleadoAutenticado);
    }
}