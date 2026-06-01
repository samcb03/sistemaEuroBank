package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorEliminarSucursal implements EventHandler<ActionEvent> {

    private final ControladorSucursales controladorSucursales;

    public ManejadorEliminarSucursal(ControladorSucursales controladorSucursales) {
        this.controladorSucursales = controladorSucursales;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorSucursales.solicitarEliminarSucursal();
    }
}