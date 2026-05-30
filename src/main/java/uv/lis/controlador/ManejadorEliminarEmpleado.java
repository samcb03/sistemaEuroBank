package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorEliminarEmpleado implements EventHandler<ActionEvent> {

    private final ControladorEmpleados controladorEmpleados;

    public ManejadorEliminarEmpleado(ControladorEmpleados controladorEmpleados) {
        this.controladorEmpleados = controladorEmpleados;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorEmpleados.solicitarEliminarEmpleado();
    }
}