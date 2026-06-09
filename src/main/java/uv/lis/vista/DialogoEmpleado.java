package uv.lis.vista;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import uv.lis.modelo.Cajero;
import uv.lis.modelo.DatosFormularioEmpleado;
import uv.lis.modelo.CatalogoEspecializacionEjecutivo;
import uv.lis.modelo.CatalogoGenero;
import uv.lis.modelo.CatalogoNivelAccesoGerente;
import uv.lis.modelo.CatalogoRol;
import uv.lis.modelo.EjecutivoCuenta;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.Gerente;

public class DialogoEmpleado {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String TITULO_AGREGAR = "Agregar empleado";
    private static final String TITULO_EDITAR = "Editar empleado";

    private final TextField campoId;
    private final TextField campoNombre;
    private final TextField campoDireccion;
    private final DatePicker selectorFechaNacimiento;
    private final ComboBox<String> selectorGenero;
    private final TextField campoSalario;
    private final TextField campoUsuario;
    private final PasswordField campoContrasenia;
    private final ComboBox<String> selectorRol;
    private final TextField campoHoraInicio;
    private final TextField campoHoraFin;
    private final TextField campoVentanilla;
    private final TextField campoClientes;
    private final ComboBox<String> selectorEspecializacion;
    private final ComboBox<String> selectorNivelAcceso;
    private final TextField campoExperiencia;
    private final GridPane seccionCajero;
    private final GridPane seccionEjecutivo;
    private final GridPane seccionGerente;
    private final ButtonType tipoBotonGuardar;
    private final Dialog<ButtonType> dialogo;

    public DialogoEmpleado() {
        this.campoId = new TextField();
        this.campoNombre = new TextField();
        this.campoDireccion = new TextField();
        this.selectorFechaNacimiento = new DatePicker();
        this.selectorGenero = crearSelectorGenero();
        this.campoSalario = new TextField();
        this.campoUsuario = new TextField();
        this.campoContrasenia = new PasswordField();
        this.selectorRol = crearSelectorRol();
        this.campoHoraInicio = new TextField();
        this.campoHoraFin = new TextField();
        this.campoVentanilla = new TextField();
        this.campoClientes = new TextField();
        this.selectorEspecializacion = crearSelectorEspecializacion();
        this.selectorNivelAcceso = crearSelectorNivelAcceso();
        this.campoExperiencia = new TextField();
        this.seccionCajero = construirSeccionCajero();
        this.seccionEjecutivo = construirSeccionEjecutivo();
        this.seccionGerente = construirSeccionGerente();
        this.tipoBotonGuardar = new ButtonType("Guardar", ButtonData.OK_DONE);
        this.dialogo = construirDialogo();
        registrarManejadorTipo();
    }

    private ComboBox<String> crearSelectorGenero() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.getItems().addAll(CatalogoGenero.valores());
        return selector;
    }

    private ComboBox<String> crearSelectorRol() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.getItems().addAll(CatalogoRol.valores());
        return selector;
    }

    private ComboBox<String> crearSelectorEspecializacion() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.getItems().addAll(CatalogoEspecializacionEjecutivo.valores());
        return selector;
    }

    private ComboBox<String> crearSelectorNivelAcceso() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.getItems().addAll(CatalogoNivelAccesoGerente.valores());
        return selector;
    }

    private Dialog<ButtonType> construirDialogo() {
        Dialog<ButtonType> dialogoConstruido = new Dialog<ButtonType>();
        dialogoConstruido.setResizable(true);
        dialogoConstruido.getDialogPane().getButtonTypes().addAll(tipoBotonGuardar, ButtonType.CANCEL);
        dialogoConstruido.getDialogPane().setContent(construirContenido());
        return dialogoConstruido;
    }

    private VBox construirContenido() {
        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(15));
        contenido.getChildren().addAll(construirSeccionComun(), seccionCajero, seccionEjecutivo, seccionGerente);
        return contenido;
    }

    private GridPane construirSeccionComun() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "ID:", campoId);
        agregarFila(panel, 1, "Nombre completo:", campoNombre);
        agregarFila(panel, 2, "Direccion:", campoDireccion);
        agregarFila(panel, 3, "Fecha de nacimiento:", selectorFechaNacimiento);
        agregarFila(panel, 4, "Genero:", selectorGenero);
        agregarFila(panel, 5, "Salario:", campoSalario);
        agregarFila(panel, 6, "Usuario:", campoUsuario);
        agregarFila(panel, 7, "Contrasena:", campoContrasenia);
        agregarFila(panel, 8, "Tipo de empleado:", selectorRol);
        return panel;
    }

    private GridPane construirSeccionCajero() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Hora inicio (HH:mm):", campoHoraInicio);
        agregarFila(panel, 1, "Hora fin (HH:mm):", campoHoraFin);
        agregarFila(panel, 2, "Numero de ventanilla:", campoVentanilla);
        return panel;
    }

    private GridPane construirSeccionEjecutivo() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Clientes asignados:", campoClientes);
        agregarFila(panel, 1, "Especializacion:", selectorEspecializacion);
        return panel;
    }

    private GridPane construirSeccionGerente() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Nivel de acceso:", selectorNivelAcceso);
        agregarFila(panel, 1, "Anios de experiencia:", campoExperiencia);
        return panel;
    }

    private GridPane crearPanelCampos() {
        GridPane panel = new GridPane();
        panel.setHgap(10);
        panel.setVgap(8);
        return panel;
    }

    private void agregarFila(GridPane panel, int fila, String etiqueta, Node control) {
        panel.add(new Label(etiqueta), 0, fila);
        panel.add(control, 1, fila);
    }

    private void registrarManejadorTipo() {
        selectorRol.valueProperty().addListener(new ManejadorSeleccionTipo(this));
    }

    public void establecerVentanaPropietaria(Window ventanaPropietaria) {
        dialogo.initOwner(ventanaPropietaria);
    }

    public void prepararParaAgregar() {
        dialogo.setTitle(TITULO_AGREGAR);
        limpiarCampos();
        campoId.setDisable(false);
        selectorRol.setDisable(false);
        actualizarVisibilidadSecciones();
    }

    public void prepararParaEditar(Empleado empleado) {
        dialogo.setTitle(TITULO_EDITAR);
        cargarCamposComunes(empleado);
        cargarCamposEspecificos(empleado);
        campoId.setDisable(true);
        selectorRol.setDisable(true);
        actualizarVisibilidadSecciones();
    }

    public boolean mostrarYConfirmar() {
        Optional<ButtonType> respuesta = dialogo.showAndWait();
        boolean confirmado = respuesta.isPresent() && respuesta.get() == tipoBotonGuardar;
        return confirmado;
    }

    public DatosFormularioEmpleado obtenerDatosFormulario() {
        DatosFormularioEmpleado datos = new DatosFormularioEmpleado.Constructor()
                .conIdEmpleado(campoId.getText())
                .conNombreCompleto(campoNombre.getText())
                .conDireccion(campoDireccion.getText())
                .conFechaNacimiento(selectorFechaNacimiento.getValue())
                .conGenero(selectorGenero.getValue())
                .conSalarioTexto(campoSalario.getText())
                .conNombreUsuario(campoUsuario.getText())
                .conContrasenia(campoContrasenia.getText())
                .conRol(selectorRol.getValue())
                .conHoraInicioTurnoTexto(campoHoraInicio.getText())
                .conHoraFinTurnoTexto(campoHoraFin.getText())
                .conNumeroVentanillaTexto(campoVentanilla.getText())
                .conNumeroClientesTexto(campoClientes.getText())
                .conEspecializacion(selectorEspecializacion.getValue())
                .conNivelAcceso(selectorNivelAcceso.getValue())
                .conAniosExperienciaTexto(campoExperiencia.getText())
                .construir();
        return datos;
    }

    private void limpiarCampos() {
        campoId.clear();
        campoNombre.clear();
        campoDireccion.clear();
        selectorFechaNacimiento.setValue(null);
        selectorGenero.setValue(null);
        campoSalario.clear();
        campoUsuario.clear();
        campoContrasenia.clear();
        selectorRol.setValue(null);
        campoHoraInicio.clear();
        campoHoraFin.clear();
        campoVentanilla.clear();
        campoClientes.clear();
        selectorEspecializacion.setValue(null);
        selectorNivelAcceso.setValue(null);
        campoExperiencia.clear();
    }

    private void cargarCamposComunes(Empleado empleado) {
        campoId.setText(empleado.getIdEmpleado());
        campoNombre.setText(empleado.getNombreCompleto());
        campoDireccion.setText(empleado.getDireccion());
        selectorFechaNacimiento.setValue(empleado.getFechaNacimiento());
        selectorGenero.setValue(empleado.getGenero());
        campoSalario.setText(Double.toString(empleado.getSalario()));
        campoUsuario.setText(empleado.getNombreUsuario());
        campoContrasenia.setText(empleado.getContrasenia());
        selectorRol.setValue(empleado.obtenerRol());
    }

    private void cargarCamposEspecificos(Empleado empleado) {
        if (empleado instanceof Cajero) {
            cargarCamposCajero((Cajero) empleado);
        } else if (empleado instanceof EjecutivoCuenta) {
            cargarCamposEjecutivo((EjecutivoCuenta) empleado);
        } else if (empleado instanceof Gerente) {
            cargarCamposGerente((Gerente) empleado);
        }
    }

    private void cargarCamposCajero(Cajero cajero) {
        campoHoraInicio.setText(cajero.getHoraInicioTurno().format(FORMATO_HORA));
        campoHoraFin.setText(cajero.getHoraFinTurno().format(FORMATO_HORA));
        campoVentanilla.setText(Integer.toString(cajero.getNumeroVentanilla()));
    }

    private void cargarCamposEjecutivo(EjecutivoCuenta ejecutivo) {
        campoClientes.setText(Integer.toString(ejecutivo.getNumeroClientesAsignados()));
        selectorEspecializacion.setValue(ejecutivo.getEspecializacion());
    }

    private void cargarCamposGerente(Gerente gerente) {
        selectorNivelAcceso.setValue(gerente.getNivelAcceso());
        campoExperiencia.setText(Integer.toString(gerente.getAniosExperiencia()));
    }

    private void actualizarVisibilidadSecciones() {
        String rolSeleccionado = selectorRol.getValue();
        establecerVisibilidad(seccionCajero, CatalogoRol.CAJERO.equals(rolSeleccionado));
        establecerVisibilidad(seccionEjecutivo, CatalogoRol.EJECUTIVO.equals(rolSeleccionado));
        establecerVisibilidad(seccionGerente, CatalogoRol.GERENTE.equals(rolSeleccionado));
        ajustarTamanioVentana();
    }

    private void ajustarTamanioVentana() {
        Scene escenaDialogo = dialogo.getDialogPane().getScene();
        if (escenaDialogo != null && escenaDialogo.getWindow() != null) {
            escenaDialogo.getWindow().sizeToScene();
        }
    }

    private void establecerVisibilidad(Node nodo, boolean visible) {
        nodo.setVisible(visible);
        nodo.setManaged(visible);
    }

    private static class ManejadorSeleccionTipo implements ChangeListener<String> {

        private final DialogoEmpleado dialogoEmpleado;

        private ManejadorSeleccionTipo(DialogoEmpleado dialogoEmpleado) {
            this.dialogoEmpleado = dialogoEmpleado;
        }

        @Override
        public void changed(ObservableValue<? extends String> propiedadObservada,
                            String valorAnterior,
                            String valorNuevo) {
            dialogoEmpleado.actualizarVisibilidadSecciones();
        }
    }
}