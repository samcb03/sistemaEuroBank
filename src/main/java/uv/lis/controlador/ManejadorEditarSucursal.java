package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorEditarSucursal implements EventHandler<ActionEvent> {

    private final ControladorSucursales controladorSucursales;

    public ManejadorEditarSucursal(ControladorSucursales controladorSucursales) {
        this.controladorSucursales = controladorSucursales;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorSucursales.solicitarEditarSucursal();
    }
}