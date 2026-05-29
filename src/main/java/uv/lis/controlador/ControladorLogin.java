package uv.lis.controlador;

import uv.lis.modelo.Empleado;
import uv.lis.modelo.ResultadoAutenticacion;
import uv.lis.modelo.Rol;
import uv.lis.modelo.ServicioAutenticacion;
import uv.lis.vista.VistaLogin;

public class ControladorLogin {

    private final VistaLogin vistaLogin;
    private final ServicioAutenticacion servicioAutenticacion;

    public ControladorLogin(VistaLogin vistaLogin, ServicioAutenticacion servicioAutenticacion) {
        this.vistaLogin = vistaLogin;
        this.servicioAutenticacion = servicioAutenticacion;
    }

    public void iniciar() {
        registrarManejadores();
    }

    private void registrarManejadores() {
        ManejadorAccionAcceder manejadorAcceso = new ManejadorAccionAcceder(this);
        vistaLogin.obtenerBotonAcceder().setOnAction(manejadorAcceso);
        vistaLogin.obtenerCampoNombreUsuario().setOnAction(manejadorAcceso);
        vistaLogin.obtenerCampoContrasenia().setOnAction(manejadorAcceso);

        ManejadorCambioCredenciales manejadorCambio = new ManejadorCambioCredenciales(this);
        vistaLogin.obtenerCampoNombreUsuario().textProperty().addListener(manejadorCambio);
        vistaLogin.obtenerCampoContrasenia().textProperty().addListener(manejadorCambio);
    }

    public void procesarSolicitudAcceso() {
        String nombreUsuario = vistaLogin.obtenerNombreUsuarioIngresado();
        String contrasenia = vistaLogin.obtenerContraseniaIngresada();
        if (camposIncompletos(nombreUsuario, contrasenia)) {
            vistaLogin.mostrarMensajeError("Debe ingresar usuario y contrasena.");
        } else {
            evaluarCredenciales(nombreUsuario, contrasenia);
        }
    }

    public void limpiarMensaje() {
        vistaLogin.limpiarMensaje();
    }

    private void evaluarCredenciales(String nombreUsuario, String contrasenia) {
        ResultadoAutenticacion resultado = servicioAutenticacion.autenticar(nombreUsuario, contrasenia);
        if (resultado.fueAutenticado()) {
            atenderAccesoConcedido(resultado.obtenerEmpleadoAutenticado().get());
        } else {
            atenderAccesoDenegado(resultado.obtenerMensaje());
        }
    }

    private void atenderAccesoConcedido(Empleado empleado) {
        String saludo = "Bienvenido " + empleado.getNombreCompleto()
                + " (" + empleado.obtenerDescripcionPuesto() + ").";
        vistaLogin.mostrarMensajeExito(saludo);
        abrirVentanaPrincipal(empleado);
    }

    private void atenderAccesoDenegado(String mensaje) {
        vistaLogin.mostrarMensajeError(mensaje);
        vistaLogin.limpiarContrasenia();
    }

    private boolean camposIncompletos(String nombreUsuario, String contrasenia) {
        boolean incompletos = nombreUsuario.isEmpty() || contrasenia.isEmpty();
        return incompletos;
    }

    private void abrirVentanaPrincipal(Empleado empleado) {
        Rol rol = empleado.obtenerRol();
        switch (rol) {
            case ADMINISTRADOR:
                // abrir Ventana Principal con acceso total (root)
                break;
            case GERENTE:
                // abrir Ventana Principal de gerente
                break;
            case CAJERO:
                // abrir Ventana Principal de cajero
                break;
            case EJECUTIVO:
                // abrir Ventana Principal de ejecutivo
                break;
            default:
                break;
        }
    }
}