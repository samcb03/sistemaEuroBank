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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uv.lis.modelo.Cliente;

import java.util.List;
import java.util.Optional;

public class VistaCliente {

    private static final double ANCHO_ESCENA       = 860;
    private static final double ALTO_ESCENA        = 500;
    private static final double ANCHO_COLUMNA_RFC  = 140;
    private static final double ANCHO_COLUMNA_NOMBRE   = 160;
    private static final double ANCHO_COLUMNA_APELLIDOS = 160;
    private static final double ANCHO_COLUMNA_CORREO   = 200;
    private static final double ANCHO_COLUMNA_TELEFONO = 120;

    private final TableView<Cliente> tablaClientes;
    private final ObservableList<Cliente> clientesObservables;
    private final Button botonAgregar;
    private final Button botonEditar;
    private final Button botonEliminar;
    private final Button botonConsultarSaldo;
    private final TextField campoBusqueda;
    private final Label labelResultadoSaldo;
    private final Scene escena;

    public VistaCliente() {
        this.clientesObservables = FXCollections.observableArrayList();
        this.tablaClientes       = crearTabla();
        this.botonAgregar        = new Button("Agregar");
        this.botonEditar         = new Button("Editar");
        this.botonEliminar       = new Button("Eliminar");
        this.botonConsultarSaldo = new Button("Consultar Saldo");
        this.campoBusqueda       = new TextField();
        this.labelResultadoSaldo = new Label();
        this.escena              = construirEscena();
    }

    private Scene construirEscena() {
        Label titulo = new Label("Administración de Clientes");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        HBox barraBusqueda = construirBarraBusqueda();
        VBox.setVgrow(tablaClientes, Priority.ALWAYS);

        VBox contenedor = new VBox(12);
        contenedor.setPadding(new Insets(20));
        contenedor.getChildren().addAll(
            titulo,
            barraBusqueda,
            tablaClientes,
            labelResultadoSaldo,
            construirBarraBotones()
        );
        return new Scene(contenedor, ANCHO_ESCENA, ALTO_ESCENA);
    }

    private HBox construirBarraBusqueda() {
        campoBusqueda.setPromptText("Buscar por RFC/CURP...");
        campoBusqueda.setPrefWidth(250);
        HBox barra = new HBox(10, new Label("Buscar:"), campoBusqueda, botonConsultarSaldo);
        barra.setAlignment(Pos.CENTER_LEFT);
        return barra;
    }

    private HBox construirBarraBotones() {
        HBox barra = new HBox(10);
        barra.setAlignment(Pos.CENTER_RIGHT);
        barra.getChildren().addAll(botonAgregar, botonEditar, botonEliminar);
        return barra;
    }

    private TableView<Cliente> crearTabla() {
        TableView<Cliente> tabla = new TableView<>(clientesObservables);
        tabla.getColumns().add(
            crearColumna("RFC/CURP", "rfcCurp", ANCHO_COLUMNA_RFC));
        tabla.getColumns().add(
            crearColumna("Nombre", "nombre", ANCHO_COLUMNA_NOMBRE));
        tabla.getColumns().add(
            crearColumna("Apellidos", "apellidos", ANCHO_COLUMNA_APELLIDOS));
        tabla.getColumns().add(
            crearColumna("Correo", "correo", ANCHO_COLUMNA_CORREO));
        tabla.getColumns().add(
            crearColumna("Teléfono", "telefono", ANCHO_COLUMNA_TELEFONO));
        return tabla;
    }

    private <T> TableColumn<Cliente, T> crearColumna(String titulo,
            String propiedad, double ancho) {
        TableColumn<Cliente, T> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        columna.setPrefWidth(ancho);
        return columna;
    }

    public void refrescarTabla(List<Cliente> clientes) {
        clientesObservables.setAll(clientes);
    }

    public void mostrarResultadoSaldo(String resultado) {
        labelResultadoSaldo.setText(resultado);
    }

    public void mostrarMensajeError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Información", mensaje);
    }

    public boolean confirmarEliminacion(String nombreCliente) {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea eliminar al cliente " + nombreCliente + "?");
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

    public Scene obtenerEscena()              { return escena; }
    public Button obtenerBotonAgregar()       { return botonAgregar; }
    public Button obtenerBotonEditar()        { return botonEditar; }
    public Button obtenerBotonEliminar()      { return botonEliminar; }
    public Button obtenerBotonConsultarSaldo(){ return botonConsultarSaldo; }
    public TextField obtenerCampoBusqueda()   { return campoBusqueda; }
    public Cliente obtenerClienteSeleccionado() {
        return tablaClientes.getSelectionModel().getSelectedItem();
    }
}

