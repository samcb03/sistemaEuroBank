package uv.lis.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import uv.lis.modelo.excepcion.ClienteNoEncontradoException;

public class RepositorioCliente {

    private static final int INDICE_NO_ENCONTRADO = -1;
    private final List<Cliente> clientes;

    public RepositorioCliente() {
        this.clientes = new ArrayList<Cliente>();
        cargarClientesIniciales();
    }

    private void cargarClientesIniciales() {
        clientes.add(new Cliente("C001", "Carlos Gómez", "Av. Universidad 100", LocalDate.of(1990, 5, 12), Genero.MASCULINO));
        clientes.add(new Cliente("C002", "Elena Ruiz", "Calle Norte 20", LocalDate.of(1995, 8, 22), Genero.FEMENINO));
    }

    public Optional<Cliente> buscarPorId(String idCliente) {
        return clientes.stream()
                .filter(c -> c.getIdCliente().equals(idCliente))
                .findFirst();
    }

    public List<Cliente> obtenerTodos() {
        return new ArrayList<Cliente>(clientes);
    }

    public void agregar(Cliente cliente) throws ClienteDuplicadoException {
        if (existeId(cliente.getIdCliente())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el ID " + cliente.getIdCliente() + ".");
        }
        clientes.add(cliente);
    }

    public void actualizar(Cliente cliente) throws ClienteNoEncontradoException {
        int indice = obtenerIndicePorId(cliente.getIdCliente());
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new ClienteNoEncontradoException("No se encontró el cliente con ID " + cliente.getIdCliente() + ".");
        }
        clientes.set(indice, cliente);
    }

    public void eliminar(String idCliente) throws ClienteNoEncontradoException {
        int indice = obtenerIndicePorId(idCliente);
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new ClienteNoEncontradoException("No se encontró el cliente con ID " + idCliente + ".");
        }
        clientes.remove(indice);
    }

    private boolean existeId(String idCliente) {
        return obtenerIndicePorId(idCliente) != INDICE_NO_ENCONTRADO;
    }

    private int obtenerIndicePorId(String idCliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdCliente().equals(idCliente)) {
                return i;
            }
        }
        return INDICE_NO_ENCONTRADO;
    }
}