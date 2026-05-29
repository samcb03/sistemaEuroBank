package uv.lis.controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ManejadorCambioCredenciales implements ChangeListener<String> {

    private final ControladorLogin controladorLogin;

    public ManejadorCambioCredenciales(ControladorLogin controladorLogin) {
        this.controladorLogin = controladorLogin;
    }

    @Override
    public void changed(ObservableValue<? extends String> propiedadObservada,
                        String valorAnterior,
                        String valorNuevo) {
        controladorLogin.limpiarMensaje();
    }
}