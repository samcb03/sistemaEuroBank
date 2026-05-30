package uv.lis.controlador;

import uv.lis.modelo.DatosFormularioEmpleado;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.FabricaEmpleados;
import uv.lis.modelo.RepositorioEmpleados;
import uv.lis.modelo.excepcion.DatosEmpleadoInvalidosException;
import uv.lis.modelo.excepcion.EmpleadoDuplicadoException;
import uv.lis.modelo.excepcion.EmpleadoNoEncontradoException;
import uv.lis.vista.DialogoEmpleado;
import uv.lis.vista.VistaEmpleados;

public class ControladorEmpleados {

    private final VistaEmpleados vistaEmpleados;
    private final RepositorioEmpleados repositorioEmpleados;
    private final FabricaEmpleados fabricaEmpleados;
    private final DialogoEmpleado dialogoEmpleado;

    private boolean propietarioDialogoEstablecido;

    public ControladorEmpleados(VistaEmpleados vistaEmpleados, RepositorioEmpleados repositorioEmpleados) {
        this.vistaEmpleados = vistaEmpleados;
        this.repositorioEmpleados = repositorioEmpleados;
        this.fabricaEmpleados = new FabricaEmpleados();
        this.dialogoEmpleado = new DialogoEmpleado();
        this.propietarioDialogoEstablecido = false;
    }

    public void iniciar() {
        registrarManejadores();
        vistaEmpleados.refrescarTabla(repositorioEmpleados.obtenerTodos());
    }

    private void registrarManejadores() {
        vistaEmpleados.obtenerBotonAgregar().setOnAction(new ManejadorAgregarEmpleado(this));
        vistaEmpleados.obtenerBotonEditar().setOnAction(new ManejadorEditarEmpleado(this));
        vistaEmpleados.obtenerBotonEliminar().setOnAction(new ManejadorEliminarEmpleado(this));
    }

    public void solicitarAgregarEmpleado() {
        dialogoEmpleado.prepararParaAgregar();
        procesarCaptura(true);
    }

    public void solicitarEditarEmpleado() {
        Empleado empleadoSeleccionado = vistaEmpleados.obtenerEmpleadoSeleccionado();
        if (empleadoSeleccionado == null) {
            vistaEmpleados.mostrarMensajeError("Seleccione un empleado para editar.");
        } else {
            dialogoEmpleado.prepararParaEditar(empleadoSeleccionado);
            procesarCaptura(false);
        }
    }

    public void solicitarEliminarEmpleado() {
        Empleado empleadoSeleccionado = vistaEmpleados.obtenerEmpleadoSeleccionado();
        if (empleadoSeleccionado == null) {
            vistaEmpleados.mostrarMensajeError("Seleccione un empleado para eliminar.");
        } else {
            confirmarYEliminar(empleadoSeleccionado);
        }
    }

    private void procesarCaptura(boolean esAlta) {
        asegurarPropietarioDialogo();
        boolean capturaPendiente = true;
        while (capturaPendiente && dialogoEmpleado.mostrarYConfirmar()) {
            capturaPendiente = !intentarGuardar(esAlta);
        }
    }

    private boolean intentarGuardar(boolean esAlta) {
        boolean guardadoExitoso;
        try {
            Empleado empleado = construirEmpleadoDesdeDialogo();
            persistirEmpleado(empleado, esAlta);
            vistaEmpleados.refrescarTabla(repositorioEmpleados.obtenerTodos());
            guardadoExitoso = true;
        } catch (DatosEmpleadoInvalidosException | EmpleadoDuplicadoException
                | EmpleadoNoEncontradoException excepcion) {
            vistaEmpleados.mostrarMensajeError(excepcion.getMessage());
            guardadoExitoso = false;
        }
        return guardadoExitoso;
    }

    private Empleado construirEmpleadoDesdeDialogo() throws DatosEmpleadoInvalidosException {
        DatosFormularioEmpleado datos = dialogoEmpleado.obtenerDatosFormulario();
        Empleado empleado = fabricaEmpleados.crearDesdeDatos(datos);
        return empleado;
    }

    private void persistirEmpleado(Empleado empleado, boolean esAlta)
            throws EmpleadoDuplicadoException, EmpleadoNoEncontradoException {
        if (esAlta) {
            repositorioEmpleados.agregar(empleado);
        } else {
            repositorioEmpleados.actualizar(empleado);
        }
    }

    private void confirmarYEliminar(Empleado empleado) {
        if (vistaEmpleados.confirmarEliminacion(empleado.getNombreCompleto())) {
            ejecutarEliminacion(empleado);
        }
    }

    private void ejecutarEliminacion(Empleado empleado) {
        try {
            repositorioEmpleados.eliminar(empleado.getIdEmpleado());
            vistaEmpleados.refrescarTabla(repositorioEmpleados.obtenerTodos());
            vistaEmpleados.mostrarMensajeInformacion("Empleado eliminado correctamente.");
        } catch (EmpleadoNoEncontradoException excepcion) {
            vistaEmpleados.mostrarMensajeError(excepcion.getMessage());
        }
    }

    private void asegurarPropietarioDialogo() {
        if (!propietarioDialogoEstablecido) {
            dialogoEmpleado.establecerVentanaPropietaria(vistaEmpleados.obtenerEscena().getWindow());
            propietarioDialogoEstablecido = true;
        }
    }
}
