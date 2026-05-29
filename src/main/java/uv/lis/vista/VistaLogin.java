package uv.lis.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Construye y expone la interfaz grafica de la ventana de acceso.
 * No contiene logica de negocio ni de control: solo arma los componentes,
 * los entrega al controlador y ofrece operaciones para mostrar mensajes.
 */
public class VistaLogin {

    private static final double ANCHO_ESCENA = 380;
    private static final double ALTO_ESCENA = 300;
    private static final String COLOR_ERROR = "-fx-text-fill: #c0392b;";
    private static final String COLOR_EXITO = "-fx-text-fill: #1e8449;";

    private final TextField campoNombreUsuario;
    private final PasswordField campoContrasenia;
    private final Button botonAcceder;
    private final Label etiquetaMensaje;
    private final Scene escena;

    public VistaLogin() {
        this.campoNombreUsuario = new TextField();
        this.campoContrasenia = new PasswordField();
        this.botonAcceder = new Button("Acceder");
        this.etiquetaMensaje = new Label();
        this.escena = construirEscena();
    }

    private Scene construirEscena() {
        Label etiquetaTitulo = new Label("Sistema EuroBank");
        etiquetaTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        GridPane panelCampos = construirPanelCampos();
        botonAcceder.setMaxWidth(Double.MAX_VALUE);

        VBox contenedorPrincipal = new VBox(15);
        contenedorPrincipal.setAlignment(Pos.CENTER);
        contenedorPrincipal.setPadding(new Insets(25));
        contenedorPrincipal.getChildren().addAll(etiquetaTitulo, panelCampos, botonAcceder, etiquetaMensaje);

        Scene escenaConstruida = new Scene(contenedorPrincipal, ANCHO_ESCENA, ALTO_ESCENA);
        return escenaConstruida;
    }

    private GridPane construirPanelCampos() {
        Label etiquetaUsuario = new Label("Usuario:");
        Label etiquetaContrasenia = new Label("Contrasena:");

        campoNombreUsuario.setPromptText("Ingrese su usuario");
        campoContrasenia.setPromptText("Ingrese su contrasena");

        GridPane panelCampos = new GridPane();
        panelCampos.setAlignment(Pos.CENTER);
        panelCampos.setHgap(10);
        panelCampos.setVgap(12);
        panelCampos.add(etiquetaUsuario, 0, 0);
        panelCampos.add(campoNombreUsuario, 1, 0);
        panelCampos.add(etiquetaContrasenia, 0, 1);
        panelCampos.add(campoContrasenia, 1, 1);
        return panelCampos;
    }

    public Scene obtenerEscena() {
        return escena;
    }

    public TextField obtenerCampoNombreUsuario() {
        return campoNombreUsuario;
    }

    public PasswordField obtenerCampoContrasenia() {
        return campoContrasenia;
    }

    public Button obtenerBotonAcceder() {
        return botonAcceder;
    }

    public String obtenerNombreUsuarioIngresado() {
        String nombreUsuario = campoNombreUsuario.getText().trim();
        return nombreUsuario;
    }

    public String obtenerContraseniaIngresada() {
        String contrasenia = campoContrasenia.getText();
        return contrasenia;
    }

    public void mostrarMensajeError(String mensaje) {
        etiquetaMensaje.setStyle(COLOR_ERROR);
        etiquetaMensaje.setText(mensaje);
    }

    public void mostrarMensajeExito(String mensaje) {
        etiquetaMensaje.setStyle(COLOR_EXITO);
        etiquetaMensaje.setText(mensaje);
    }

    public void limpiarMensaje() {
        etiquetaMensaje.setText("");
    }

    public void limpiarContrasenia() {
        campoContrasenia.clear();
    }
}