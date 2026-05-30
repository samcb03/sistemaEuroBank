package uv.lis.vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uv.lis.modelo.Transaccion;

import java.util.List;

public class VistaTransaccion {

    private static final double ANCHO_ESCENA         = 900;
    private static final double ALTO_ESCENA          = 560;
    private static final double ANCHO_COLUMNA_TIPO   = 120;
    private static final double ANCHO_COLUMNA_MONTO  = 110;
    private static final double ANCHO_COLUMNA_FECHA  = 160;
    private static final double ANCHO_COLUMNA_ORIGEN = 140;
    private static final double ANCHO_COLUMNA_DESTINO = 140;

    private final TableView<Transaccion> tablaTransacciones;
    private final ObservableList<Transaccion> transaccionesObservables;

    private final ComboBox<String> comboTipoTransaccion;
    private final TextField campoCuentaOrigen;
    private final TextField campoCuentaDestino;
    private final TextField campoMonto;
    private final TextField campoSucursal;

    private final Button botonEjecutar;
    private final Button botonLimpiar;

    private final Scene escena;

    public VistaTransaccion() {
        this.transaccionesObservables = FXCollections.observableArrayList();
        this.tablaTransacciones       = crearTabla();
        this.comboTipoTransaccion     = new ComboBox<>(
            FXCollections.observableArrayList(
                Transaccion.DEPOSITO,
                Transaccion.RETIRO,
                Transaccion.TRANSFERENCIA));
        this.campoCuentaOrigen  = new TextField();
        this.campoCuentaDestino = new TextField();
        this.campoMonto         = new TextField();
        this.campoSucursal      = new TextField();
        this.botonEjecutar      = new Button("Ejecutar");
        this.botonLimpiar       = new Button("Limpiar");
        this.escena             = construirEscena();
    }

    private Scene construirEscena() {
        Label titulo = new Label("Registro de Transacciones");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox.setVgrow(tablaTransacciones, Priority.ALWAYS);

        VBox contenedor = new VBox(12);
        contenedor.setPadding(new Insets(20));
        contenedor.getChildren().addAll(
            titulo,
            construirFormulario(),
            construirBarraBotones(),
            new Label("Historial de transacciones:"),
            tablaTransacciones
        );
        return new Scene(contenedor, ANCHO_ESCENA, ALTO_ESCENA);
    }

    private GridPane construirFormulario() {
        campoCuentaOrigen.setPromptText("Número de cuenta origen");
        campoCuentaDestino.setPromptText("Número de cuenta destino");
        campoMonto.setPromptText("Monto");
        campoSucursal.setPromptText("ID Sucursal");
        comboTipoTransaccion.setPromptText("Tipo de transacción");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Tipo:"),            0, 0);
        grid.add(comboTipoTransaccion,          1, 0);
        grid.add(new Label("Cuenta Origen:"),   0, 1);
        grid.add(campoCuentaOrigen,             1, 1);
        grid.add(new Label("Cuenta Destino:"),  0, 2);
        grid.add(campoCuentaDestino,            1, 2);
        grid.add(new Label("Monto:"),           0, 3);
        grid.add(campoMonto,                    1, 3);
        grid.add(new Label("Sucursal:"),        0, 4);
        grid.add(campoSucursal,                 1, 4);

        return grid;
    }

    private HBox construirBarraBotones() {
        HBox barra = new HBox(10);
        barra.setAlignment(Pos.CENTER_RIGHT);
        barra.getChildren().addAll(botonLimpiar, botonEjecutar);
        return barra;
    }

    private TableView<Transaccion> crearTabla() {
        TableView<Transaccion> tabla = new TableView<>(transaccionesObservables);
        tabla.getColumns().add(
            crearColumna("Tipo", "tipo", ANCHO_COLUMNA_TIPO));
        tabla.getColumns().add(
            crearColumna("Monto", "monto", ANCHO_COLUMNA_MONTO));
        tabla.getColumns().add(
            crearColumna("Fecha/Hora", "fechaHora", ANCHO_COLUMNA_FECHA));
        tabla.getColumns().add(
            crearColumna("Cuenta Origen", "cuentaOrigen", ANCHO_COLUMNA_ORIGEN));
        tabla.getColumns().add(
            crearColumna("Cuenta Destino", "cuentaDestino", ANCHO_COLUMNA_DESTINO));
        return tabla;
    }

    private <T> TableColumn<Transaccion, T> crearColumna(String titulo,
            String propiedad, double ancho) {
        TableColumn<Transaccion, T> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        columna.setPrefWidth(ancho);
        return columna;
    }

    public void refrescarTabla(List<Transaccion> transacciones) {
        transaccionesObservables.setAll(transacciones);
    }

    public void limpiarFormulario() {
        comboTipoTransaccion.setValue(null);
        campoCuentaOrigen.clear();
        campoCuentaDestino.clear();
        campoMonto.clear();
        campoSucursal.clear();
    }

    public void mostrarMensajeError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Información", mensaje);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public Scene obtenerEscena()                    { return escena; }
    public Button obtenerBotonEjecutar()            { return botonEjecutar; }
    public Button obtenerBotonLimpiar()             { return botonLimpiar; }
    public ComboBox<String> obtenerComboTipo()      { return comboTipoTransaccion; }
    public String obtenerCuentaOrigen()             { return campoCuentaOrigen.getText().trim(); }
    public String obtenerCuentaDestino()            { return campoCuentaDestino.getText().trim(); }
    public String obtenerMonto()                    { return campoMonto.getText().trim(); }
    public String obtenerSucursal()                 { return campoSucursal.getText().trim(); }
}