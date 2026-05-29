package uv.lis;

import javafx.application.Application;
import javafx.stage.Stage;
import uv.lis.controlador.ControladorLogin;
import uv.lis.modelo.RepositorioEmpleados;
import uv.lis.modelo.ServicioAutenticacion;
import uv.lis.vista.VistaLogin;

public class AplicacionEuroBank extends Application {

    private static final String TITULO_VENTANA = "EuroBank - Acceso al Sistema";

    @Override
    public void start(Stage escenarioPrincipal) {
        RepositorioEmpleados repositorioEmpleados = new RepositorioEmpleados();
        ServicioAutenticacion servicioAutenticacion = new ServicioAutenticacion(repositorioEmpleados);
        VistaLogin vistaLogin = new VistaLogin();
        ControladorLogin controladorLogin = new ControladorLogin(vistaLogin, servicioAutenticacion);
        controladorLogin.iniciar();
        configurarEscenario(escenarioPrincipal, vistaLogin);
    }

    private void configurarEscenario(Stage escenarioPrincipal, VistaLogin vistaLogin) {
        escenarioPrincipal.setTitle(TITULO_VENTANA);
        escenarioPrincipal.setScene(vistaLogin.obtenerEscena());
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.show();
    }

    public static void main(String[] argumentos) {
        launch(argumentos);
    }
}