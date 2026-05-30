package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorAgregarEmpleado implements EventHandler<ActionEvent> {

    private final ControladorEmpleados controladorEmpleados;

    public ManejadorAgregarEmpleado(ControladorEmpleados controladorEmpleados) {
        this.controladorEmpleados = controladorEmpleados;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorEmpleados.solicitarAgregarEmpleado();
    }
}

