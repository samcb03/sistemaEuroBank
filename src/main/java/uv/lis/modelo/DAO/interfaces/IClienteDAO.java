package uv.lis.modelo.DAO.interfaces;

import java.util.List;
import java.util.Optional;

import uv.lis.modelo.Cliente;
import uv.lis.modelo.excepcion.ClienteDuplicadoException;
import uv.lis.modelo.excepcion.ClienteNoEncontradoException;

public interface IClienteDAO {
    List<Cliente> obtenerTodos();
    void agregar(Cliente cliente) throws ClienteDuplicadoException;
    void actualizar(Cliente cliente) throws ClienteNoEncontradoException;
    void eliminar(String idCliente) throws ClienteNoEncontradoException;
    List<Cliente> buscarPorCriterio(String filtro);
    Optional<Cliente> buscarPorRfc(String rfcCurp);
}
