package uv.lis.controlador;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import uv.lis.controlador.ControladorMenuPrincipal.OpcionMenu;

public class ManejadorOpcionMenu implements EventHandler<ActionEvent> {

    private final ControladorMenuPrincipal controladorMenuPrincipal;
    private final OpcionMenu opcionMenu;

    public ManejadorOpcionMenu(ControladorMenuPrincipal controladorMenuPrincipal,
                               OpcionMenu opcionMenu) {
        this.controladorMenuPrincipal = controladorMenuPrincipal;
        this.opcionMenu = opcionMenu;
    }

    @Override
    public void handle(ActionEvent evento) {
        controladorMenuPrincipal.atenderOpcion(opcionMenu);
    }
}