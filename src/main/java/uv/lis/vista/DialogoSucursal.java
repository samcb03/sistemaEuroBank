package uv.lis.vista;

import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import uv.lis.modelo.Sucursal;
import uv.lis.modelo.ValidadorCampos;

public class DialogoSucursal {

    private final TextField campoNumeroIdentificacion;
    private final TextField campoNombre;
    private final TextField campoDireccion;
    private final TextField campoTelefono;
    private final TextField campoCorreo;
    private final ComboBox<String> selectorGerente;
    private final TextField campoPersonaContacto;
    private final ButtonType tipoBotonGuardar;
    private final Dialog<ButtonType> dialogo;

    public DialogoSucursal() {
        this.campoNumeroIdentificacion = new TextField();
        this.campoNombre = new TextField();
        this.campoDireccion = new TextField();
        this.campoTelefono = new TextField();
        this.campoCorreo = new TextField();
        this.selectorGerente = crearSelectorGerente();
        this.campoPersonaContacto = new TextField();
        this.tipoBotonGuardar = new ButtonType("Guardar", ButtonData.OK_DONE);
        this.dialogo = construirDialogo();
    }

    private ComboBox<String> crearSelectorGerente() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.setEditable(true);
        selector.setMaxWidth(Double.MAX_VALUE);
        return selector;
    }

    private Dialog<ButtonType> construirDialogo() {
        Dialog<ButtonType> dialogoConstruido = new Dialog<ButtonType>();
        dialogoConstruido.setResizable(true);
        dialogoConstruido.getDialogPane().getButtonTypes().addAll(tipoBotonGuardar, ButtonType.CANCEL);
        dialogoConstruido.getDialogPane().setContent(construirFormulario());
        return dialogoConstruido;
    }

    private GridPane construirFormulario() {
        GridPane formulario = new GridPane();
        formulario.setPadding(new Insets(15));
        formulario.setHgap(10);
        formulario.setVgap(8);
        agregarFila(formulario, 0, "Numero de identificacion:", campoNumeroIdentificacion);
        agregarFila(formulario, 1, "Nombre:", campoNombre);
        agregarFila(formulario, 2, "Direccion:", campoDireccion);
        agregarFila(formulario, 3, "Telefono:", campoTelefono);
        agregarFila(formulario, 4, "Correo:", campoCorreo);
        agregarFila(formulario, 5, "Nombre del gerente:", selectorGerente);
        agregarFila(formulario, 6, "Persona de contacto:", campoPersonaContacto);
        return formulario;
    }

    private void agregarFila(GridPane formulario, int fila, String etiqueta, Node control) {
        formulario.add(new Label(etiqueta), 0, fila);
        formulario.add(control, 1, fila);
    }

    public void establecerVentanaPropietaria(Window ventanaPropietaria) {
        dialogo.initOwner(ventanaPropietaria);
    }

    public void prepararParaAgregar() {
        dialogo.setTitle("Agregar sucursal");
        limpiarCampos();
        campoNumeroIdentificacion.setDisable(false);
    }

    public void prepararParaEditar(Sucursal sucursal) {
        dialogo.setTitle("Editar sucursal");
        campoNumeroIdentificacion.setText(sucursal.getNumeroIdentificacion());
        campoNombre.setText(sucursal.getNombre());
        campoDireccion.setText(sucursal.getDireccion());
        campoTelefono.setText(sucursal.getTelefono());
        campoCorreo.setText(sucursal.getCorreo());
        selectorGerente.setValue(sucursal.getNombreGerente());
        campoPersonaContacto.setText(sucursal.getPersonaContacto());
        campoNumeroIdentificacion.setDisable(true);
    }

    public boolean mostrarYConfirmar() {
        Optional<ButtonType> respuesta = dialogo.showAndWait();
        return respuesta.isPresent() && respuesta.get() == tipoBotonGuardar;
    }

    public Sucursal construirSucursalDesdeFormulario() {
        String numeroIdentificacion = campoNumeroIdentificacion.getText().trim();
        String nombre = campoNombre.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String correo = campoCorreo.getText().trim();
        String gerente = gerenteSeleccionado();
        String personaContacto = campoPersonaContacto.getText().trim();

        ValidadorCampos.validarObligatorio(numeroIdentificacion, "Número de identificación");
        ValidadorCampos.validarObligatorio(nombre, "Nombre");
        ValidadorCampos.validarObligatorio(direccion, "Dirección");
        ValidadorCampos.validarTelefono(telefono);
        ValidadorCampos.validarCorreo(correo);
        ValidadorCampos.validarObligatorio(gerente, "Gerente");
        ValidadorCampos.validarObligatorio(personaContacto, "Persona de contacto");

        Sucursal sucursal = new Sucursal(
                numeroIdentificacion, nombre, direccion,
                telefono, correo, gerente, personaContacto);
        return sucursal;
    }

    public void cargarGerentes(List<String> nombresGerentes) {
        String seleccionActual = selectorGerente.getValue();
        selectorGerente.getItems().setAll(nombresGerentes);
        selectorGerente.setValue(seleccionActual);
    }

    private String gerenteSeleccionado() {
        String valor = selectorGerente.getValue();
        if (valor == null) {
            valor = "";
        }
        return valor.trim();
    }

    private void limpiarCampos() {
        campoNumeroIdentificacion.clear();
        campoNombre.clear();
        campoDireccion.clear();
        campoTelefono.clear();
        campoCorreo.clear();
        selectorGerente.setValue(null);
        campoPersonaContacto.clear();
    }
}