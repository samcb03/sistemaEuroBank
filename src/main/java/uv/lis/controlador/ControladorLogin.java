package uv.lis.controlador;

import javafx.stage.Stage;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.RepositorioEmpleados;
import uv.lis.modelo.ResultadoAutenticacion;
import uv.lis.modelo.ServicioAutenticacion;
import uv.lis.vista.VistaLogin;
import uv.lis.vista.VistaMenuPrincipal;

public class ControladorLogin {

    private static final String TITULO_VENTANA_MENU = "EuroBank - Menu Principal";

    private final VistaLogin vistaLogin;
    private final ServicioAutenticacion servicioAutenticacion;
    private final RepositorioEmpleados repositorioEmpleados;

    public ControladorLogin(VistaLogin vistaLogin,
                            ServicioAutenticacion servicioAutenticacion,
                            RepositorioEmpleados repositorioEmpleados) {
        this.vistaLogin = vistaLogin;
        this.servicioAutenticacion = servicioAutenticacion;
        this.repositorioEmpleados = repositorioEmpleados;
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
        Stage escenarioLogin = obtenerEscenarioLogin();
        VistaMenuPrincipal vistaMenu = new VistaMenuPrincipal();
        Stage escenarioMenu = crearEscenarioMenu(vistaMenu);
        ControladorMenuPrincipal controladorMenu = new ControladorMenuPrincipal(
                vistaMenu, empleado, repositorioEmpleados, escenarioMenu, escenarioLogin);
        controladorMenu.iniciar();
        escenarioLogin.hide();
        escenarioMenu.show();
    }

    private Stage obtenerEscenarioLogin() {
        Stage escenarioLogin = (Stage) vistaLogin.obtenerEscena().getWindow();
        return escenarioLogin;
    }

    private Stage crearEscenarioMenu(VistaMenuPrincipal vistaMenu) {
        Stage escenarioMenu = new Stage();
        escenarioMenu.setTitle(TITULO_VENTANA_MENU);
        escenarioMenu.setScene(vistaMenu.obtenerEscena());
        escenarioMenu.setResizable(false);
        return escenarioMenu;
    }
}
