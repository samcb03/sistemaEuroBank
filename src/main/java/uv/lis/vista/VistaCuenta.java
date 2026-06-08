package uv.lis.vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uv.lis.modelo.CuentaBancaria;

import java.util.List;
import java.util.Optional;

public class VistaCuenta {

    private static final double ANCHO_ESCENA          = 860;
    private static final double ALTO_ESCENA           = 480;
    private static final double ANCHO_COLUMNA_NUMERO  = 140;
    private static final double ANCHO_COLUMNA_TIPO    = 120;
    private static final double ANCHO_COLUMNA_SALDO   = 110;
    private static final double ANCHO_COLUMNA_LIMITE  = 110;
    private static final double ANCHO_COLUMNA_CLIENTE = 160;

    private final TableView<CuentaBancaria> tablaCuentas;
    private final ObservableList<CuentaBancaria> cuentasObservables;
    private final Button botonAgregar;
    private final Button botonCerrar;
    private final Button botonExportarCSV;
    private final Button botonExportarPDF;
    private final Scene escena;

    public VistaCuenta() {
        this.cuentasObservables = FXCollections.observableArrayList();
        this.tablaCuentas       = crearTabla();
        this.botonAgregar       = new Button("Abrir Cuenta");
        this.botonCerrar        = new Button("Cerrar Cuenta");
        this.botonExportarCSV   = new Button("Exportar CSV");
        this.botonExportarPDF   = new Button("Exportar PDF");
        this.escena             = construirEscena();
    }

    private Scene construirEscena() {
        Label titulo = new Label("Administración de Cuentas Bancarias");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox.setVgrow(tablaCuentas, Priority.ALWAYS);

        VBox contenedor = new VBox(12);
        contenedor.setPadding(new Insets(20));
        contenedor.getChildren().addAll(
            titulo,
            tablaCuentas,
            construirBarraBotones()
        );
        return new Scene(contenedor, ANCHO_ESCENA, ALTO_ESCENA);
    }

    private HBox construirBarraBotones() {
        HBox barraExportar = new HBox(10, botonExportarCSV, botonExportarPDF);
        barraExportar.setAlignment(Pos.CENTER_LEFT);

        HBox barraAcciones = new HBox(10, botonAgregar, botonCerrar);
        barraAcciones.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(barraAcciones, Priority.ALWAYS);

        HBox barra = new HBox(10, barraExportar, barraAcciones);
        barra.setAlignment(Pos.CENTER);
        return barra;
    }

    private TableView<CuentaBancaria> crearTabla() {
        TableView<CuentaBancaria> tabla = new TableView<>(cuentasObservables);
        tabla.getColumns().add(
            crearColumna("No. Cuenta", "numeroCuenta", ANCHO_COLUMNA_NUMERO));
        tabla.getColumns().add(
            crearColumna("Tipo", "tipoCuenta", ANCHO_COLUMNA_TIPO));
        tabla.getColumns().add(
            crearColumna("Saldo", "saldo", ANCHO_COLUMNA_SALDO));
        tabla.getColumns().add(
            crearColumna("Límite Crédito", "limiteCredito", ANCHO_COLUMNA_LIMITE));
        tabla.getColumns().add(
            crearColumna("Cliente", "rfcCurpCliente", ANCHO_COLUMNA_CLIENTE));
        return tabla;
    }

    private <T> TableColumn<CuentaBancaria, T> crearColumna(String titulo,
            String propiedad, double ancho) {
        TableColumn<CuentaBancaria, T> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        columna.setPrefWidth(ancho);
        return columna;
    }

    public void refrescarTabla(List<CuentaBancaria> cuentas) {
        cuentasObservables.setAll(cuentas);
    }

    public void mostrarMensajeError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Información", mensaje);
    }

    public boolean confirmarCierre(String numeroCuenta) {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar cierre");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea cerrar la cuenta " + numeroCuenta + "?");
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public Scene obtenerEscena()                { return escena; }
    public Button obtenerBotonAgregar()         { return botonAgregar; }
    public Button obtenerBotonCerrar()          { return botonCerrar; }
    public Button obtenerBotonExportarCSV()     { return botonExportarCSV; }
    public Button obtenerBotonExportarPDF()     { return botonExportarPDF; }
    public CuentaBancaria obtenerCuentaSeleccionada() {
        return tablaCuentas.getSelectionModel().getSelectedItem();
    }
}