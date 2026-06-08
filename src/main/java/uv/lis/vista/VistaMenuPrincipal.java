package uv.lis.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uv.lis.modelo.CatalogoRol;
import uv.lis.modelo.Empleado;

public class VistaMenuPrincipal {

    private static final double ANCHO_ESCENA = 520;
    private static final double ALTO_ESCENA = 430;

    private final Empleado empleadoAutenticado;
    private final Button botonEmpleados;
    private final Button botonSucursales;
    private final Button botonClientes;
    private final Button botonCuentas;
    private final Button botonTransacciones;
    private final Button botonCerrarSesion;
    private final Scene escena;

    public VistaMenuPrincipal(Empleado empleadoAutenticado) {
        this.empleadoAutenticado = empleadoAutenticado;
        this.botonEmpleados = new Button("Empleados");
        this.botonSucursales = new Button("Sucursales");
        this.botonClientes = new Button("Clientes");
        this.botonCuentas = new Button("Cuentas");
        this.botonTransacciones = new Button("Transacciones");
        this.botonCerrarSesion = new Button("Cerrar sesion");
        this.escena = construirEscena();
    }

    private Scene construirEscena() {
        Label etiquetaTitulo = new Label("Menu Principal EuroBank");
        etiquetaTitulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        etiquetaTitulo.setStyle(EstilosVista.TITULO);

        Label etiquetaUsuario = new Label("Usuario: " + empleadoAutenticado.getNombreCompleto()
                + " | Rol: " + CatalogoRol.nombreVisible(empleadoAutenticado.obtenerRol()));
        etiquetaUsuario.setStyle(EstilosVista.SUBTITULO);

        GridPane panelModulos = construirPanelModulos();

        botonCerrarSesion.setStyle(EstilosVista.BOTON_SECUNDARIO);
        HBox barraSesion = new HBox(botonCerrarSesion);
        barraSesion.setAlignment(Pos.CENTER_RIGHT);

        VBox contenedorPrincipal = new VBox(18);
        contenedorPrincipal.setPadding(new Insets(25));
        contenedorPrincipal.setStyle(EstilosVista.FONDO_VENTANA);
        contenedorPrincipal.getChildren().addAll(etiquetaTitulo, etiquetaUsuario, panelModulos, barraSesion);

        return new Scene(contenedorPrincipal, ANCHO_ESCENA, ALTO_ESCENA);
    }

    private GridPane construirPanelModulos() {
        configurarBotonModulo(botonEmpleados);
        configurarBotonModulo(botonSucursales);
        configurarBotonModulo(botonClientes);
        configurarBotonModulo(botonCuentas);
        configurarBotonModulo(botonTransacciones);

        GridPane panel = new GridPane();
        panel.setAlignment(Pos.CENTER);
        panel.setHgap(12);
        panel.setVgap(12);
        panel.add(botonEmpleados, 0, 0);
        panel.add(botonSucursales, 1, 0);
        panel.add(botonClientes, 0, 1);
        panel.add(botonCuentas, 1, 1);
        panel.add(botonTransacciones, 0, 2, 2, 1);
        return panel;
    }

    private void configurarBotonModulo(Button boton) {
        boton.setMinWidth(200);
        boton.setMinHeight(55);
        boton.setStyle(EstilosVista.BOTON_PRIMARIO);
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