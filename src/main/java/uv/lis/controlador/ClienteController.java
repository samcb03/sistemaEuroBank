package uv.lis.controlador;

import uv.lis.modelo.excepcion.ClienteNoEncontradoException;
import uv.lis.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {

    private final List<Cliente> clientes;

    public ClienteController() {
        this.clientes = new ArrayList<>();
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }

    public void registrarCliente(Cliente cliente) {
        if (existeCliente(cliente.getRfcCurp())) {
            throw new IllegalArgumentException("El RFC/CURP ya está registrado.");
        }
    }

    public void eliminarCliente(String rfcCurp) throws ClienteNoEncontradoException {
        Cliente cliente = buscarClientePorRfc(rfcCurp);
        if (cliente.tieneCuentasActivas()) {
            throw new IllegalStateException("El cliente tiene cuentas activas y no puede ser eliminado.");
        }
    }

    public List<Cliente> buscarPorCriterio(String filtro) {
        if (filtro == null || filtro.isEmpty()) return obtenerTodosLosClientes();
        
        return clientes.stream()
            .filter(c -> c.getRfcCurp().toLowerCase().contains(filtro.toLowerCase()) ||
                         c.getNombre().toLowerCase().contains(filtro.toLowerCase()))
            .collect(Collectors.toList());
    }

    private Cliente buscarClientePorRfc(String rfcCurp) throws ClienteNoEncontradoException {
        return clientes.stream()
            .filter(c -> c.getRfcCurp().equalsIgnoreCase(rfcCurp))
            .findFirst()
            .orElseThrow(() -> new ClienteNoEncontradoException(rfcCurp));
    }

    private boolean existeCliente(String rfcCurp) {
        return clientes.stream().anyMatch(c -> c.getRfcCurp().equalsIgnoreCase(rfcCurp));
    }
}