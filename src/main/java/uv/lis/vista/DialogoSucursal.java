package uv.lis.vista;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import uv.lis.modelo.DatosFormularioSucursal;
import uv.lis.modelo.Sucursal;

public class DialogoSucursal {

    private static final String TITULO_AGREGAR = "Agregar sucursal";
    private static final String TITULO_EDITAR = "Editar sucursal";

    private final TextField campoId;
    private final TextField campoNombre;
    private final TextField campoDireccion;
    private final TextField campoTelefono;
    private final TextField campoCorreo;
    private final TextField campoGerente;
    private final TextField campoContacto;
    private final ButtonType tipoBotonGuardar;
    private final Dialog<ButtonType> dialogo;

    public DialogoSucursal() {
        this.campoId = new TextField();
        this.campoNombre = new TextField();
        this.campoDireccion = new TextField();
        this.campoTelefono = new TextField();
        this.campoCorreo = new TextField();
        this.campoGerente = new TextField();
        this.campoContacto = new TextField();
        this.tipoBotonGuardar = new ButtonType("Guardar", ButtonData.OK_DONE);
        this.dialogo = construirDialogo();
    }

    private Dialog<ButtonType> construirDialogo() {
        Dialog<ButtonType> dialogoConstruido = new Dialog<ButtonType>();
        dialogoConstruido.setResizable(true);
        dialogoConstruido.getDialogPane().getButtonTypes().addAll(tipoBotonGuardar, ButtonType.CANCEL);
        dialogoConstruido.getDialogPane().setContent(construirContenido());
        return dialogoConstruido;
    }

    private GridPane construirContenido() {
        GridPane panel = new GridPane();
        panel.setPadding(new Insets(15));
        panel.setHgap(10);
        panel.setVgap(8);
        agregarFila(panel, 0, "Numero de identificacion:", campoId);
        agregarFila(panel, 1, "Nombre:", campoNombre);
        agregarFila(panel, 2, "Direccion:", campoDireccion);
        agregarFila(panel, 3, "Telefono:", campoTelefono);
        agregarFila(panel, 4, "Correo:", campoCorreo);
        agregarFila(panel, 5, "Nombre del gerente:", campoGerente);
        agregarFila(panel, 6, "Persona de contacto:", campoContacto);
        return panel;
    }

    private void agregarFila(GridPane panel, int fila, String etiqueta, Node control) {
        panel.add(new Label(etiqueta), 0, fila);
        panel.add(control, 1, fila);
    }

    public void establecerVentanaPropietaria(Window ventanaPropietaria) {
        dialogo.initOwner(ventanaPropietaria);
    }

    public void prepararParaAgregar() {
        dialogo.setTitle(TITULO_AGREGAR);
        limpiarCampos();
        campoId.setDisable(false);
    }

    public void prepararParaEditar(Sucursal sucursal) {
        dialogo.setTitle(TITULO_EDITAR);
        cargarCampos(sucursal);
        campoId.setDisable(true);
    }

    public boolean mostrarYConfirmar() {
        Optional<ButtonType> respuesta = dialogo.showAndWait();
        boolean confirmado = respuesta.isPresent() && respuesta.get() == tipoBotonGuardar;
        return confirmado;
    }

    public DatosFormularioSucursal obtenerDatosFormulario() {
        DatosFormularioSucursal datos = new DatosFormularioSucursal.Constructor()
                .conIdSucursal(campoId.getText())
                .conNombre(campoNombre.getText())
                .conDireccion(campoDireccion.getText())
                .conTelefono(campoTelefono.getText())
                .conCorreo(campoCorreo.getText())
                .conNombreGerente(campoGerente.getText())
                .conPersonaContacto(campoContacto.getText())
                .construir();
        return datos;
    }

    private void limpiarCampos() {
        campoId.clear();
        campoNombre.clear();
        campoDireccion.clear();
        campoTelefono.clear();
        campoCorreo.clear();
        campoGerente.clear();
        campoContacto.clear();
    }

    private void cargarCampos(Sucursal sucursal) {
        campoId.setText(sucursal.getIdSucursal());
        campoNombre.setText(sucursal.getNombre());
        campoDireccion.setText(sucursal.getDireccion());
        campoTelefono.setText(sucursal.getTelefono());
        campoCorreo.setText(sucursal.getCorreo());
        campoGerente.setText(sucursal.getNombreGerente());
        campoContacto.setText(sucursal.getPersonaContacto());
    }
}