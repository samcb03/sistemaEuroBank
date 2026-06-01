package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorAgregarSucursal implements EventHandler<ActionEvent> {

    private final ControladorSucursales controladorSucursales;

    public ManejadorAgregarSucursal(ControladorSucursales controladorSucursales) {
        this.controladorSucursales = controladorSucursales;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorSucursales.solicitarAgregarSucursal();
    }
}