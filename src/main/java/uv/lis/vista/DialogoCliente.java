package uv.lis.vista;

import java.time.LocalDate;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import uv.lis.modelo.Cliente;
import uv.lis.modelo.ValidadorCampos;

public class DialogoCliente {

    private static final String TITULO_AGREGAR = "Agregar cliente";
    private static final String TITULO_EDITAR = "Editar cliente";

    private final TextField campoRfcCurp;
    private final TextField campoNombre;
    private final TextField campoApellidos;
    private final TextField campoNacionalidad;
    private final DatePicker selectorFechaNacimiento;
    private final TextField campoDireccion;
    private final TextField campoTelefono;
    private final TextField campoCorreo;

    private final ButtonType tipoBotonGuardar;
    private final Dialog<ButtonType> dialogo;

    public DialogoCliente() {
        this.campoRfcCurp = new TextField();
        this.campoNombre = new TextField();
        this.campoApellidos = new TextField();
        this.campoNacionalidad = new TextField();
        this.selectorFechaNacimiento = new DatePicker();
        this.campoDireccion = new TextField();
        this.campoTelefono = new TextField();
        this.campoCorreo = new TextField();
        this.tipoBotonGuardar = new ButtonType("Guardar", ButtonData.OK_DONE);
        this.dialogo = construirDialogo();
    }

    public void establecerVentanaPropietaria(Window ventanaPropietaria) {
        dialogo.initOwner(ventanaPropietaria);
    }

    public void prepararParaAgregar() {
        dialogo.setTitle(TITULO_AGREGAR);
        limpiarCampos();
        campoRfcCurp.setDisable(false);
    }

    public void prepararParaEditar(Cliente cliente) {
        dialogo.setTitle(TITULO_EDITAR);
        cargarCampos(cliente);
        campoRfcCurp.setDisable(true);
    }

    public boolean mostrarYConfirmar() {
        Optional<ButtonType> respuesta = dialogo.showAndWait();
        return respuesta.isPresent() && respuesta.get() == tipoBotonGuardar;
    }

    public Cliente obtenerCliente() {
        String rfcCurp = campoRfcCurp.getText().trim().toUpperCase();
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String nacionalidad = campoNacionalidad.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String correo = campoCorreo.getText().trim();
        LocalDate fechaNacimiento = selectorFechaNacimiento.getValue();

        ValidadorCampos.validarRfcCurp(rfcCurp);
        ValidadorCampos.validarSoloLetras(nombre, "Nombre");
        ValidadorCampos.validarSoloLetras(apellidos, "Apellidos");
        ValidadorCampos.validarSoloLetras(nacionalidad, "Nacionalidad");
        ValidadorCampos.validarFechaNacimiento(fechaNacimiento);
        ValidadorCampos.validarObligatorio(direccion, "Dirección");
        ValidadorCampos.validarTelefono(telefono);
        ValidadorCampos.validarCorreo(correo);

        Cliente cliente = new Cliente(
                rfcCurp, nombre, apellidos, nacionalidad,
                fechaNacimiento.toString(), direccion, telefono, correo);
        return cliente;
    }

    private Dialog<ButtonType> construirDialogo() {
        Dialog<ButtonType> d = new Dialog<>();
        d.getDialogPane().setContent(construirFormulario());
        d.getDialogPane().getButtonTypes().addAll(tipoBotonGuardar, ButtonType.CANCEL);
        return d;
    }

    private GridPane construirFormulario() {
        configurarPrompts();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));

        grid.add(new Label("RFC/CURP:"), 0, 0);
        grid.add(campoRfcCurp,
                1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(campoNombre,
                1, 1);
        grid.add(new Label("Apellidos:"), 0, 2);
        grid.add(campoApellidos,
                1, 2);
        grid.add(new Label("Nacionalidad:"), 0, 3);
        grid.add(campoNacionalidad,
                1, 3);
        grid.add(new Label("Fecha nacimiento:"), 0, 4);
        grid.add(selectorFechaNacimiento,
                1, 4);
        grid.add(new Label("Dirección:"), 0, 5);
        grid.add(campoDireccion,
                1, 5);
        grid.add(new Label("Teléfono:"), 0, 6);
        grid.add(campoTelefono,
                1, 6);
        grid.add(new Label("Correo:"), 0, 7);
        grid.add(campoCorreo,
                1, 7);

        return grid;
    }

    private void configurarPrompts() {
        campoRfcCurp.setPromptText("RFC O CURP");
        campoNombre.setPromptText("Nombre(s)");
        campoApellidos.setPromptText("Apellido paterno y materno");
        campoNacionalidad.setPromptText("Ej. Mexicana");
        campoDireccion.setPromptText("Calle, número, colonia");
        campoTelefono.setPromptText("10 dígitos");
        campoCorreo.setPromptText("ejemplo@correo.com");
    }

    private void limpiarCampos() {
        campoRfcCurp.clear();
        campoNombre.clear();
        campoApellidos.clear();
        campoNacionalidad.clear();
        selectorFechaNacimiento.setValue(null);
        campoDireccion.clear();
        campoTelefono.clear();
        campoCorreo.clear();
    }

    private void cargarCampos(Cliente cliente) {
        campoRfcCurp.setText(cliente.getRfcCurp());
        campoNombre.setText(cliente.getNombre());
        campoApellidos.setText(cliente.getApellidos());
        campoNacionalidad.setText(cliente.getNacionalidad());
        campoDireccion.setText(cliente.getDireccion());
        campoTelefono.setText(cliente.getTelefono());
        campoCorreo.setText(cliente.getCorreo());

        if (cliente.getFechaNacimiento() != null && !cliente.getFechaNacimiento().isEmpty()) {
            try {
                selectorFechaNacimiento.setValue(
                        java.time.LocalDate.parse(cliente.getFechaNacimiento()));
            } catch (Exception ex) {
                selectorFechaNacimiento.setValue(null);
            }
        } else {
            selectorFechaNacimiento.setValue(null);
        }
    }
}
