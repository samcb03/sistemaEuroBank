package uv.lis.controlador;

import javafx.stage.Stage;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.SucursalDAO;
import uv.lis.modelo.DAO.implementacion.EmpleadoDAO;
import uv.lis.modelo.ResultadoAutenticacion;
import uv.lis.modelo.ServicioAutenticacion;
import uv.lis.vista.VistaLogin;
import uv.lis.vista.VistaMenuPrincipal;

public class ControladorLogin {

    private static final String TITULO_VENTANA_MENU = "EuroBank - Menu Principal";

    private final VistaLogin vistaLogin;
    private final ServicioAutenticacion servicioAutenticacion;
    private final EmpleadoDAO repositorioEmpleados;
    private final SucursalDAO repositorioSucursales;

    public ControladorLogin(VistaLogin vistaLogin,
                            ServicioAutenticacion servicioAutenticacion,
                            EmpleadoDAO repositorioEmpleados) {
        this.vistaLogin = vistaLogin;
        this.servicioAutenticacion = servicioAutenticacion;
        this.repositorioEmpleados = repositorioEmpleados;
        this.repositorioSucursales = new SucursalDAO();
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
        vistaLogin.mostrarMensajeExito("Bienvenido " + empleado.getNombreCompleto() + ".");
        abrirMenuPrincipal(empleado);
    }

    private void atenderAccesoDenegado(String mensaje) {
        vistaLogin.mostrarMensajeError(mensaje);
        vistaLogin.limpiarContrasenia();
    }

    private boolean camposIncompletos(String nombreUsuario, String contrasenia) {
        return nombreUsuario.isEmpty() || contrasenia.isEmpty();
    }

    private void abrirMenuPrincipal(Empleado empleado) {
        Stage escenarioPrincipal = (Stage) vistaLogin.obtenerEscena().getWindow();
        VistaMenuPrincipal vistaMenuPrincipal = new VistaMenuPrincipal(empleado);
        ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(
                vistaMenuPrincipal,
                repositorioEmpleados,
                repositorioSucursales,
                escenarioPrincipal,
                vistaLogin);
        controladorMenuPrincipal.iniciar(empleado.obtenerPermisos());
        escenarioPrincipal.setTitle(TITULO_VENTANA_MENU);
        escenarioPrincipal.setScene(vistaMenuPrincipal.obtenerEscena());
        escenarioPrincipal.centerOnScreen();
    }
}