package uv.lis.controlador;

import uv.lis.modelo.DatosFormularioSucursal;
import uv.lis.modelo.FabricaSucursales;
import uv.lis.modelo.RepositorioSucursales;
import uv.lis.modelo.Sucursal;
import uv.lis.modelo.excepcion.DatosSucursalInvalidosException;
import uv.lis.modelo.excepcion.SucursalDuplicadaException;
import uv.lis.modelo.excepcion.SucursalNoEncontradaException;
import uv.lis.vista.DialogoSucursal;
import uv.lis.vista.VistaSucursales;

public class ControladorSucursales {

    private final VistaSucursales vistaSucursales;
    private final RepositorioSucursales repositorioSucursales;
    private final FabricaSucursales fabricaSucursales;
    private final DialogoSucursal dialogoSucursal;

    private boolean propietarioDialogoEstablecido;

    public ControladorSucursales(VistaSucursales vistaSucursales,
                                 RepositorioSucursales repositorioSucursales) {
        this.vistaSucursales = vistaSucursales;
        this.repositorioSucursales = repositorioSucursales;
        this.fabricaSucursales = new FabricaSucursales();
        this.dialogoSucursal = new DialogoSucursal();
        this.propietarioDialogoEstablecido = false;
    }

    public void iniciar() {
        registrarManejadores();
        vistaSucursales.refrescarTabla(repositorioSucursales.obtenerTodas());
    }

    private void registrarManejadores() {
        vistaSucursales.obtenerBotonAgregar().setOnAction(new ManejadorAgregarSucursal(this));
        vistaSucursales.obtenerBotonEditar().setOnAction(new ManejadorEditarSucursal(this));
        vistaSucursales.obtenerBotonEliminar().setOnAction(new ManejadorEliminarSucursal(this));
    }

    public void solicitarAgregarSucursal() {
        asegurarPropietarioDialogo();
        dialogoSucursal.prepararParaAgregar();
        procesarCaptura(true);
    }

    public void solicitarEditarSucursal() {
        Sucursal sucursalSeleccionada = vistaSucursales.obtenerSucursalSeleccionada();
        if (sucursalSeleccionada == null) {
            vistaSucursales.mostrarMensajeError("Seleccione una sucursal para editar.");
        } else {
            asegurarPropietarioDialogo();
            dialogoSucursal.prepararParaEditar(sucursalSeleccionada);
            procesarCaptura(false);
        }
    }

    public void solicitarEliminarSucursal() {
        Sucursal sucursalSeleccionada = vistaSucursales.obtenerSucursalSeleccionada();
        if (sucursalSeleccionada == null) {
            vistaSucursales.mostrarMensajeError("Seleccione una sucursal para eliminar.");
        } else {
            confirmarYEliminar(sucursalSeleccionada);
        }
    }

    private void procesarCaptura(boolean esAlta) {
        boolean capturaPendiente = true;
        while (capturaPendiente && dialogoSucursal.mostrarYConfirmar()) {
            capturaPendiente = !intentarGuardar(esAlta);
        }
    }

    private boolean intentarGuardar(boolean esAlta) {
        boolean guardadoExitoso;
        try {
            Sucursal sucursal = construirSucursalDesdeDialogo();
            almacenarSucursal(sucursal, esAlta);
            vistaSucursales.refrescarTabla(repositorioSucursales.obtenerTodas());
            guardadoExitoso = true;
        } catch (DatosSucursalInvalidosException | SucursalDuplicadaException
                | SucursalNoEncontradaException excepcion) {
            vistaSucursales.mostrarMensajeError(excepcion.getMessage());
            guardadoExitoso = false;
        }
        return guardadoExitoso;
    }

    private Sucursal construirSucursalDesdeDialogo() throws DatosSucursalInvalidosException {
        DatosFormularioSucursal datos = dialogoSucursal.obtenerDatosFormulario();
        Sucursal sucursal = fabricaSucursales.crearDesdeDatos(datos);
        return sucursal;
    }

    private void almacenarSucursal(Sucursal sucursal, boolean esAlta)
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
            repositorioSucursales.eliminar(sucursal.getIdSucursal());
            vistaSucursales.refrescarTabla(repositorioSucursales.obtenerTodas());
            vistaSucursales.mostrarMensajeInformacion("Sucursal eliminada correctamente.");
        } catch (SucursalNoEncontradaException excepcion) {
            vistaSucursales.mostrarMensajeError(excepcion.getMessage());
        }
    }

    private void asegurarPropietarioDialogo() {
        if (!propietarioDialogoEstablecido) {
            dialogoSucursal.establecerVentanaPropietaria(vistaSucursales.obtenerEscena().getWindow());
            propietarioDialogoEstablecido = true;
        }
    }
}