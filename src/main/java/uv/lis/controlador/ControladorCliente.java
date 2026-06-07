package uv.lis.controlador;

import uv.lis.modelo.Cliente;
import uv.lis.modelo.DAO.implementacion.ClienteDAO;
import uv.lis.modelo.excepcion.ClienteDuplicadoException;
import uv.lis.modelo.excepcion.ClienteNoEncontradoException;
import uv.lis.vista.DialogoCliente;
import uv.lis.vista.VistaCliente;

import java.util.List;
import java.util.Optional;

public class ControladorCliente {

    private final ClienteDAO repositorioCliente;
    private final DialogoCliente     dialogoCliente;
    private       VistaCliente       vista;
    private       boolean            propietarioDialogoEstablecido;

    public ControladorCliente() {
        this.repositorioCliente            = new ClienteDAO();
        this.dialogoCliente                = new DialogoCliente();
        this.propietarioDialogoEstablecido = false;
    }

    public void iniciar(VistaCliente vista) {
        this.vista = vista;
        vista.refrescarTabla(obtenerTodosLosClientes());
        registrarManejadores();
    }

    private void registrarManejadores() {
        vista.obtenerBotonAgregar().setOnAction(e -> solicitarAgregarCliente());
        vista.obtenerBotonEditar().setOnAction(e -> solicitarEditarCliente());
        vista.obtenerBotonEliminar().setOnAction(e -> solicitarEliminarCliente());
        vista.obtenerBotonConsultarSaldo().setOnAction(e -> consultarSaldo());

        vista.obtenerCampoBusqueda().textProperty().addListener(
            (obs, anterior, nuevo) -> vista.refrescarTabla(buscarPorCriterio(nuevo))
        );
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return repositorioCliente.obtenerTodos();
    }

    public void registrarCliente(Cliente cliente) throws ClienteDuplicadoException {
        repositorioCliente.agregar(cliente);
    }

    public void actualizarCliente(Cliente clienteModificado)
            throws ClienteNoEncontradoException {
        repositorioCliente.actualizar(clienteModificado);
    }

    public void eliminarCliente(String rfcCurp)
            throws ClienteNoEncontradoException {
        Optional<Cliente> clienteEncontrado = repositorioCliente.buscarPorRfc(rfcCurp);
        if (!clienteEncontrado.isPresent()) {
            throw new ClienteNoEncontradoException(
                "No se encontró ningún cliente con RFC/CURP: " + rfcCurp);
        }
        if (clienteEncontrado.get().tieneCuentasActivas()) {
            throw new IllegalStateException(
                "El cliente tiene cuentas activas y no puede ser eliminado.");
        }
        repositorioCliente.eliminar(rfcCurp);
    }

    public List<Cliente> buscarPorCriterio(String filtro) {
        return repositorioCliente.buscarPorCriterio(filtro);
    }

    private void solicitarAgregarCliente() {
        asegurarPropietarioDialogo();
        dialogoCliente.prepararParaAgregar();
        procesarCaptura(true);
    }

    private void solicitarEditarCliente() {
        Cliente seleccionado = vista.obtenerClienteSeleccionado();
        if (seleccionado == null) {
            vista.mostrarMensajeError("Seleccione un cliente para editar.");
            return;
        }
        asegurarPropietarioDialogo();
        dialogoCliente.prepararParaEditar(seleccionado);
        procesarCaptura(false);
    }

    private void solicitarEliminarCliente() {
        Cliente seleccionado = vista.obtenerClienteSeleccionado();
        if (seleccionado == null) {
            vista.mostrarMensajeError("Seleccione un cliente para eliminar.");
            return;
        }
        confirmarYEliminar(seleccionado);
    }

    private void consultarSaldo() {
        Cliente seleccionado = vista.obtenerClienteSeleccionado();
        if (seleccionado == null) {
            vista.mostrarMensajeError("Seleccione un cliente para consultar su saldo.");
            return;
        }
        if (!seleccionado.tieneCuentasActivas()) {
            vista.mostrarResultadoSaldo(
                seleccionado.getNombreCompleto() + " no tiene cuentas asociadas.");
            return;
        }
        String resultado = "Cuentas de " + seleccionado.getNombreCompleto()
            + ": " + String.join(", ", seleccionado.getNumerosCuenta());
        vista.mostrarResultadoSaldo(resultado);
    }

    private void procesarCaptura(boolean esAlta) {
        boolean capturaPendiente = true;
        while (capturaPendiente && dialogoCliente.mostrarYConfirmar()) {
            capturaPendiente = !intentarGuardar(esAlta);
        }
    }

    private boolean intentarGuardar(boolean esAlta) {
        try {
            Cliente cliente = dialogoCliente.obtenerCliente();
            if (esAlta) {
                registrarCliente(cliente);
            } else {
                actualizarCliente(cliente);
            }
            vista.refrescarTabla(obtenerTodosLosClientes());
            return true;
        } catch (ClienteDuplicadoException | ClienteNoEncontradoException ex) {
            vista.mostrarMensajeError(ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError("Datos inválidos: " + ex.getMessage());
            return false;
        }
    }

    private void confirmarYEliminar(Cliente cliente) {
        if (vista.confirmarEliminacion(cliente.getNombreCompleto())) {
            try {
                eliminarCliente(cliente.getRfcCurp());
                vista.refrescarTabla(obtenerTodosLosClientes());
                vista.mostrarMensajeInformacion("Cliente eliminado correctamente.");
            } catch (ClienteNoEncontradoException ex) {
                vista.mostrarMensajeError(ex.getMessage());
            } catch (IllegalStateException ex) {
                vista.mostrarMensajeError(ex.getMessage());
            }
        }
    }

    private void asegurarPropietarioDialogo() {
        if (!propietarioDialogoEstablecido) {
            dialogoCliente.establecerVentanaPropietaria(
                vista.obtenerEscena().getWindow());
            propietarioDialogoEstablecido = true;
        }
    }
}