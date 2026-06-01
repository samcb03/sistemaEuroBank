package uv.lis.vista;

import java.util.List;
import java.util.Optional;
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
import uv.lis.modelo.Sucursal;

public class VistaSucursales {

    private static final double ANCHO_ESCENA = 900;
    private static final double ALTO_ESCENA = 500;

    private final TableView<Sucursal> tablaSucursales;
    private final ObservableList<Sucursal> sucursalesObservables;
    private final Button botonAgregar;
    private final Button botonEditar;
    private final Button botonEliminar;
    private final Scene escena;

    public VistaSucursales() {
        this.sucursalesObservables = FXCollections.observableArrayList();
        this.tablaSucursales = crearTabla();
        this.botonAgregar = new Button("Agregar");
        this.botonEditar = new Button("Editar");
        this.botonEliminar = new Button("Eliminar");
        this.escena = construirEscena();
    }

    private Scene construirEscena() {
        Label etiquetaTitulo = new Label("Administracion de Sucursales");
        etiquetaTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        HBox barraBotones = construirBarraBotones();
        VBox.setVgrow(tablaSucursales, Priority.ALWAYS);

        VBox contenedorPrincipal = new VBox(12);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.getChildren().addAll(etiquetaTitulo, tablaSucursales, barraBotones);

        return new Scene(contenedorPrincipal, ANCHO_ESCENA, ALTO_ESCENA);
    }

    private HBox construirBarraBotones() {
        HBox barraBotones = new HBox(10);
        barraBotones.setAlignment(Pos.CENTER_RIGHT);
        barraBotones.getChildren().addAll(botonAgregar, botonEditar, botonEliminar);
        return barraBotones;
    }

    private TableView<Sucursal> crearTabla() {
        TableView<Sucursal> tabla = new TableView<Sucursal>(sucursalesObservables);
        tabla.getColumns().add(this.<String>crearColumna("ID", "numeroIdentificacion", 110));
        tabla.getColumns().add(this.<String>crearColumna("Nombre", "nombre", 150));
        tabla.getColumns().add(this.<String>crearColumna("Direccion", "direccion", 190));
        tabla.getColumns().add(this.<String>crearColumna("Telefono", "telefono", 110));
        tabla.getColumns().add(this.<String>crearColumna("Correo", "correo", 160));
        tabla.getColumns().add(this.<String>crearColumna("Gerente", "nombreGerente", 130));
        tabla.getColumns().add(this.<String>crearColumna("Contacto", "personaContacto", 130));
        return tabla;
    }

    private <T> TableColumn<Sucursal, T> crearColumna(String titulo, String propiedad, double ancho) {
        TableColumn<Sucursal, T> columna = new TableColumn<Sucursal, T>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<Sucursal, T>(propiedad));
        columna.setPrefWidth(ancho);
        return columna;
    }

    public Scene obtenerEscena() {
        return escena;
    }

    public Button obtenerBotonAgregar() {
        return botonAgregar;
    }

    public Button obtenerBotonEditar() {
        return botonEditar;
    }

    public Button obtenerBotonEliminar() {
        return botonEliminar;
    }

    public Sucursal obtenerSucursalSeleccionada() {
        return tablaSucursales.getSelectionModel().getSelectedItem();
    }

    public void refrescarTabla(List<Sucursal> sucursales) {
        sucursalesObservables.setAll(sucursales);
    }

    public void mostrarMensajeError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Informacion", mensaje);
    }

    public boolean confirmarEliminacion(String nombreSucursal) {
        Alert alertaConfirmacion = new Alert(AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar eliminacion");
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText("Desea eliminar la sucursal " + nombreSucursal + "?");
        Optional<ButtonType> respuesta = alertaConfirmacion.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
