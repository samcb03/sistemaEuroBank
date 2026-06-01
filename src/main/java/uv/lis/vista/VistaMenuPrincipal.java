package uv.lis.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VistaMenuPrincipal {

    private static final double ANCHO_ESCENA = 360;
    private static final double ALTO_ESCENA = 460;

    private final Label etiquetaUsuario;
    private final Button botonEmpleados;
    private final Button botonSucursales;
    private final Button botonClientes;
    private final Button botonCuentas;
    private final Button botonTransacciones;
    private final Button botonCerrarSesion;
    private final Scene escena;

    public VistaMenuPrincipal() {
        this.etiquetaUsuario = new Label();
        this.botonEmpleados = new Button("Empleados");
        this.botonSucursales = new Button("Sucursales");
        this.botonClientes = new Button("Clientes");
        this.botonCuentas = new Button("Cuentas");
        this.botonTransacciones = new Button("Transacciones");
        this.botonCerrarSesion = new Button("Cerrar sesion");
        this.escena = construirEscena();
    }

    private Scene construirEscena() {
        Label etiquetaTitulo = new Label("EuroBank - Menu Principal");
        etiquetaTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox contenedorPrincipal = new VBox(12);
        contenedorPrincipal.setAlignment(Pos.TOP_CENTER);
        contenedorPrincipal.setPadding(new Insets(25));
        contenedorPrincipal.getChildren().addAll(
                etiquetaTitulo,
                etiquetaUsuario,
                new Separator(),
                botonEmpleados,
                botonSucursales,
                botonClientes,
                botonCuentas,
                botonTransacciones,
                new Separator(),
                botonCerrarSesion);
        aplicarAnchoCompleto();

        Scene escenaConstruida = new Scene(contenedorPrincipal, ANCHO_ESCENA, ALTO_ESCENA);
        return escenaConstruida;
    }

    private void aplicarAnchoCompleto() {
        botonEmpleados.setMaxWidth(Double.MAX_VALUE);
        botonSucursales.setMaxWidth(Double.MAX_VALUE);
        botonClientes.setMaxWidth(Double.MAX_VALUE);
        botonCuentas.setMaxWidth(Double.MAX_VALUE);
        botonTransacciones.setMaxWidth(Double.MAX_VALUE);
        botonCerrarSesion.setMaxWidth(Double.MAX_VALUE);
    }

    public void mostrarUsuario(String nombreCompleto, String descripcionPuesto) {
        etiquetaUsuario.setText("Sesion: " + nombreCompleto + " (" + descripcionPuesto + ")");
    }

    public Scene obtenerEscena() {
        return escena;
    }

    public Button obtenerBotonEmpleados() {
        return botonEmpleados;
    }

    public Button obtenerBotonSucursales() {
        return botonSucursales;
    }

    public Button obtenerBotonClientes() {
        return botonClientes;
    }

    public Button obtenerBotonCuentas() {
        return botonCuentas;
    }

    public Button obtenerBotonTransacciones() {
        return botonTransacciones;
    }

    public Button obtenerBotonCerrarSesion() {
        return botonCerrarSesion;
    }
}