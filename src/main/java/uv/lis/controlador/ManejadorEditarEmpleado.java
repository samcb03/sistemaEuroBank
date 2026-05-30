package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorEditarEmpleado implements EventHandler<ActionEvent> {

    private final ControladorEmpleados controladorEmpleados;

    public ManejadorEditarEmpleado(ControladorEmpleados controladorEmpleados) {
        this.controladorEmpleados = controladorEmpleados;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorEmpleados.solicitarEditarEmpleado();
    }
}

