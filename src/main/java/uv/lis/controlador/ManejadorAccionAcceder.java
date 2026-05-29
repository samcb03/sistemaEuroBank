package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManejadorAccionAcceder implements EventHandler<ActionEvent> {

    private final ControladorLogin controladorLogin;

    public ManejadorAccionAcceder(ControladorLogin controladorLogin) {
        this.controladorLogin = controladorLogin;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorLogin.procesarSolicitudAcceso();
    }
}