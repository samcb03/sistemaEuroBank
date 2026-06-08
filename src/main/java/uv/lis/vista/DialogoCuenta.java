package uv.lis.vista;

import java.util.List;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import uv.lis.modelo.CatalogoTipoCuenta;

public class DialogoCuenta {

    private static final String TITULO_AGREGAR = "Abrir cuenta";

    private final TextField campoNumeroCuenta;
    private final ComboBox<String> selectorTipo;
    private final TextField campoSaldoInicial;
    private final ComboBox<String> selectorCliente;
    private final ComboBox<String> selectorSucursal;
    private final TextField campoTasaInteres;
    private final TextField campoLimiteCredito;
    private final TextField campoRazonSocial;
    private final GridPane seccionAhorros;
    private final GridPane seccionCredito;
    private final GridPane seccionRazonSocial;
    private final ButtonType tipoBotonGuardar;
    private final Dialog<ButtonType> dialogo;

    public DialogoCuenta() {
        this.campoNumeroCuenta = new TextField();
        this.selectorTipo = crearSelectorTipo();
        this.campoSaldoInicial = new TextField();
        this.selectorCliente = new ComboBox<String>();
        this.selectorSucursal = new ComboBox<String>();
        this.campoTasaInteres = new TextField();
        this.campoLimiteCredito = new TextField();
        this.campoRazonSocial = new TextField();
        this.seccionAhorros = construirSeccionAhorros();
        this.seccionCredito = construirSeccionCredito();
        this.seccionRazonSocial = construirSeccionRazonSocial();
        this.tipoBotonGuardar = new ButtonType("Guardar", ButtonData.OK_DONE);
        this.dialogo = construirDialogo();
        registrarManejadorTipo();
    }

    private ComboBox<String> crearSelectorTipo() {
        ComboBox<String> selector = new ComboBox<String>();
        selector.getItems().addAll(CatalogoTipoCuenta.valores());
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
        contenido.getChildren().addAll(
                construirSeccionComun(), seccionAhorros, seccionCredito, seccionRazonSocial);
        return contenido;
    }

    private GridPane construirSeccionComun() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Numero de cuenta:", campoNumeroCuenta);
        agregarFila(panel, 1, "Tipo de cuenta:", selectorTipo);
        agregarFila(panel, 2, "Saldo inicial:", campoSaldoInicial);
        agregarFila(panel, 3, "Cliente (RFC/CURP):", selectorCliente);
        agregarFila(panel, 4, "Sucursal:", selectorSucursal);
        return panel;
    }

    private GridPane construirSeccionAhorros() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Tasa de interes (%):", campoTasaInteres);
        return panel;
    }

    private GridPane construirSeccionCredito() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Limite de credito:", campoLimiteCredito);
        return panel;
    }

    private GridPane construirSeccionRazonSocial() {
        GridPane panel = crearPanelCampos();
        agregarFila(panel, 0, "Razon social:", campoRazonSocial);
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
        selectorTipo.valueProperty().addListener(new ManejadorSeleccionTipoCuenta(this));
    }

    public void establecerVentanaPropietaria(Window ventanaPropietaria) {
        dialogo.initOwner(ventanaPropietaria);
    }

    public void cargarClientes(List<String> rfcClientes) {
        selectorCliente.getItems().setAll(rfcClientes);
    }

    public void cargarSucursales(List<String> idsSucursal) {
        selectorSucursal.getItems().setAll(idsSucursal);
    }

    public void prepararParaAgregar() {
        dialogo.setTitle(TITULO_AGREGAR);
        limpiarCampos();
        actualizarVisibilidadSecciones();
    }

    public boolean mostrarYConfirmar() {
        Optional<ButtonType> respuesta = dialogo.showAndWait();
        boolean confirmado = respuesta.isPresent() && respuesta.get() == tipoBotonGuardar;
        return confirmado;
    }

    private void limpiarCampos() {
        campoNumeroCuenta.clear();
        selectorTipo.setValue(null);
        campoSaldoInicial.clear();
        selectorCliente.setValue(null);
        selectorSucursal.setValue(null);
        campoTasaInteres.clear();
        campoLimiteCredito.clear();
        campoRazonSocial.clear();
    }

    private void actualizarVisibilidadSecciones() {
        String tipoSeleccionado = selectorTipo.getValue();
        boolean esAhorros = CatalogoTipoCuenta.AHORROS.equals(tipoSeleccionado);
        boolean esCorriente = CatalogoTipoCuenta.CORRIENTE.equals(tipoSeleccionado);
        boolean esEmpresarial = CatalogoTipoCuenta.EMPRESARIAL.equals(tipoSeleccionado);
        establecerVisibilidad(seccionAhorros, esAhorros);
        establecerVisibilidad(seccionCredito, esCorriente || esEmpresarial);
        establecerVisibilidad(seccionRazonSocial, esEmpresarial);
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

    private String valorSeleccionado(ComboBox<String> combo) {
        String valor = combo.getValue();
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    public String obtenerNumeroCuenta()            { return campoNumeroCuenta.getText().trim(); }
    public String obtenerTipoSeleccionado()        { return valorSeleccionado(selectorTipo); }
    public String obtenerSaldoInicialTexto()       { return campoSaldoInicial.getText().trim(); }
    public String obtenerRfcClienteSeleccionado()  { return valorSeleccionado(selectorCliente); }
    public String obtenerSucursalSeleccionada()    { return valorSeleccionado(selectorSucursal); }
    public String obtenerTasaInteresTexto()        { return campoTasaInteres.getText().trim(); }
    public String obtenerLimiteCreditoTexto()      { return campoLimiteCredito.getText().trim(); }
    public String obtenerRazonSocial()             { return campoRazonSocial.getText().trim(); }

    private static class ManejadorSeleccionTipoCuenta implements ChangeListener<String> {

        private final DialogoCuenta dialogoCuenta;

        private ManejadorSeleccionTipoCuenta(DialogoCuenta dialogoCuenta) {
            this.dialogoCuenta = dialogoCuenta;
        }

        @Override
        public void changed(ObservableValue<? extends String> propiedadObservada,
                            String valorAnterior,
                            String valorNuevo) {
            dialogoCuenta.actualizarVisibilidadSecciones();
        }
    }
}