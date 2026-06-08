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
import uv.lis.modelo.Empleado;

public class VistaEmpleados {

    private static final double ANCHO_ESCENA = 760;
    private static final double ALTO_ESCENA = 460;
    private static final double ANCHO_COLUMNA_ID = 70;
    private static final double ANCHO_COLUMNA_NOMBRE = 180;
    private static final double ANCHO_COLUMNA_PUESTO = 200;
    private static final double ANCHO_COLUMNA_USUARIO = 110;
    private static final double ANCHO_COLUMNA_SALARIO = 100;
    private static final double ANCHO_COLUMNA_EDAD = 70;

    private final TableView<Empleado> tablaEmpleados;
    private final ObservableList<Empleado> empleadosObservables;
    private final Button botonAgregar;
    private final Button botonEditar;
    private final Button botonEliminar;
    private final Scene escena;

    public VistaEmpleados() {
        this.empleadosObservables = FXCollections.observableArrayList();
        this.tablaEmpleados = crearTabla();
        this.botonAgregar = new Button("Agregar");
        this.botonEditar = new Button("Editar");
        this.botonEliminar = new Button("Eliminar");
        this.escena = construirEscena();
    }

    private Scene construirEscena() {
        Label etiquetaTitulo = new Label("Administracion de Empleados");
        etiquetaTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        etiquetaTitulo.setStyle(EstilosVista.TITULO);
        tablaEmpleados.setStyle(EstilosVista.TABLA);

        HBox barraBotones = construirBarraBotones();
        VBox.setVgrow(tablaEmpleados, Priority.ALWAYS);

        VBox contenedorPrincipal = new VBox(12);
        contenedorPrincipal.setPadding(new Insets(20));
        contenedorPrincipal.setStyle(EstilosVista.FONDO_VENTANA);
        contenedorPrincipal.getChildren().addAll(etiquetaTitulo, tablaEmpleados, barraBotones);

        Scene escenaConstruida = new Scene(contenedorPrincipal, ANCHO_ESCENA, ALTO_ESCENA);
        return escenaConstruida;
    }

    private HBox construirBarraBotones() {
        botonAgregar.setStyle(EstilosVista.BOTON_EXITO);
        botonEditar.setStyle(EstilosVista.BOTON_PRIMARIO);
        botonEliminar.setStyle(EstilosVista.BOTON_PELIGRO);
        HBox barraBotones = new HBox(10);
        barraBotones.setAlignment(Pos.CENTER_RIGHT);
        barraBotones.getChildren().addAll(botonAgregar, botonEditar, botonEliminar);
        return barraBotones;
    }

    private TableView<Empleado> crearTabla() {
        TableView<Empleado> tabla = new TableView<Empleado>(empleadosObservables);
        tabla.getColumns().add(this.<String>crearColumna("ID", "idEmpleado", ANCHO_COLUMNA_ID));
        tabla.getColumns().add(this.<String>crearColumna("Nombre", "nombreCompleto", ANCHO_COLUMNA_NOMBRE));
        tabla.getColumns().add(this.<String>crearColumna("Puesto", "descripcionPuesto", ANCHO_COLUMNA_PUESTO));
        tabla.getColumns().add(this.<String>crearColumna("Usuario", "nombreUsuario", ANCHO_COLUMNA_USUARIO));
        tabla.getColumns().add(this.<Number>crearColumna("Salario", "salario", ANCHO_COLUMNA_SALARIO));
        tabla.getColumns().add(this.<Number>crearColumna("Edad", "edad", ANCHO_COLUMNA_EDAD));
        return tabla;
    }

    private <T> TableColumn<Empleado, T> crearColumna(String titulo, String propiedad, double ancho) {
        TableColumn<Empleado, T> columna = new TableColumn<Empleado, T>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<Empleado, T>(propiedad));
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

    public Empleado obtenerEmpleadoSeleccionado() {
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        return empleadoSeleccionado;
    }

    public void refrescarTabla(List<Empleado> empleados) {
        empleadosObservables.setAll(empleados);
    }

    public void mostrarMensajeError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Informacion", mensaje);
    }

    public boolean confirmarEliminacion(String nombreEmpleado) {
        Alert alertaConfirmacion = new Alert(AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar eliminacion");
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText("Desea eliminar al empleado " + nombreEmpleado + "?");
        Optional<ButtonType> respuesta = alertaConfirmacion.showAndWait();
        boolean confirmado = respuesta.isPresent() && respuesta.get() == ButtonType.OK;
        return confirmado;
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}