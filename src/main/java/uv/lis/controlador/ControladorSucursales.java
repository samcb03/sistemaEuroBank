package uv.lis.controlador;

import uv.lis.modelo.RepositorioSucursales;
import uv.lis.modelo.Sucursal;
import uv.lis.modelo.excepcion.PersistenciaSucursalException;
import uv.lis.modelo.excepcion.SucursalDuplicadaException;
import uv.lis.modelo.excepcion.SucursalNoEncontradaException;
import uv.lis.vista.DialogoSucursal;
import uv.lis.vista.VistaSucursales;

public class ControladorSucursales {

    private final VistaSucursales vistaSucursales;
    private final RepositorioSucursales repositorioSucursales;
    private final DialogoSucursal dialogoSucursal;
    private boolean propietarioDialogoEstablecido;

    public ControladorSucursales(VistaSucursales vistaSucursales,
                                 RepositorioSucursales repositorioSucursales) {
        this.vistaSucursales = vistaSucursales;
        this.repositorioSucursales = repositorioSucursales;
        this.dialogoSucursal = new DialogoSucursal();
        this.propietarioDialogoEstablecido = false;
    }

    public void iniciar() {
        registrarManejadores();
        refrescarTabla();
    }

    private void registrarManejadores() {
        vistaSucursales.obtenerBotonAgregar().setOnAction(evento -> solicitarAgregarSucursal());
        vistaSucursales.obtenerBotonEditar().setOnAction(evento -> solicitarEditarSucursal());
        vistaSucursales.obtenerBotonEliminar().setOnAction(evento -> solicitarEliminarSucursal());
    }

    private void solicitarAgregarSucursal() {
        dialogoSucursal.prepararParaAgregar();
        procesarCaptura(true);
    }

    private void solicitarEditarSucursal() {
        Sucursal sucursalSeleccionada = vistaSucursales.obtenerSucursalSeleccionada();
        if (sucursalSeleccionada == null) {
            vistaSucursales.mostrarMensajeError("Seleccione una sucursal para editar.");
        } else {
            dialogoSucursal.prepararParaEditar(sucursalSeleccionada);
            procesarCaptura(false);
        }
    }

    private void solicitarEliminarSucursal() {
        Sucursal sucursalSeleccionada = vistaSucursales.obtenerSucursalSeleccionada();
        if (sucursalSeleccionada == null) {
            vistaSucursales.mostrarMensajeError("Seleccione una sucursal para eliminar.");
        } else {
            confirmarYEliminar(sucursalSeleccionada);
        }
    }

    private void procesarCaptura(boolean esAlta) {
        asegurarPropietarioDialogo();
        boolean capturaPendiente = true;
        while (capturaPendiente && dialogoSucursal.mostrarYConfirmar()) {
            capturaPendiente = !intentarGuardar(esAlta);
        }
    }

    private boolean intentarGuardar(boolean esAlta) {
        boolean guardadoExitoso;
        try {
            Sucursal sucursal = dialogoSucursal.construirSucursalDesdeFormulario();
            persistirSucursal(sucursal, esAlta);
            refrescarTabla();
            vistaSucursales.mostrarMensajeInformacion("Sucursal guardada correctamente.");
            guardadoExitoso = true;
        } catch (IllegalArgumentException | SucursalDuplicadaException
                | SucursalNoEncontradaException | PersistenciaSucursalException excepcion) {
            vistaSucursales.mostrarMensajeError(excepcion.getMessage());
            guardadoExitoso = false;
        }
        return guardadoExitoso;
    }

    private void persistirSucursal(Sucursal sucursal, boolean esAlta)
            throws SucursalDuplicadaException, SucursalNoEncontradaException {
        if (esAlta) {
            repositorioSucursales.agregar(sucursal);
        } else {
            repositorioSucursales.actualizar(sucursal);
        }
    }

    private void confirmarYEliminar(Sucursal sucursal) {
        if (vistaSucursales.confirmarEliminacion(sucursal.getNombre())) {
            ejecutarEliminacion(sucursal);
        }
    }

    private void ejecutarEliminacion(Sucursal sucursal) {
        try {
            repositorioSucursales.eliminar(sucursal.getNumeroIdentificacion());
            refrescarTabla();
            vistaSucursales.mostrarMensajeInformacion("Sucursal eliminada correctamente.");
        } catch (SucursalNoEncontradaException | PersistenciaSucursalException excepcion) {
            vistaSucursales.mostrarMensajeError(excepcion.getMessage());
        }
    }

    private void refrescarTabla() {
        vistaSucursales.refrescarTabla(repositorioSucursales.obtenerTodas());
    }

    private void asegurarPropietarioDialogo() {
        if (!propietarioDialogoEstablecido) {
            dialogoSucursal.establecerVentanaPropietaria(vistaSucursales.obtenerEscena().getWindow());
            propietarioDialogoEstablecido = true;
        }
    }
}
