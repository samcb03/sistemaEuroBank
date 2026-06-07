package uv.lis.modelo.DAO.interfaces;

import java.util.List;
import java.util.Optional;

import uv.lis.modelo.Empleado;
import uv.lis.modelo.excepcion.EmpleadoDuplicadoException;
import uv.lis.modelo.excepcion.EmpleadoNoEncontradoException;

public interface IEmpleadoDAO {
    List<Empleado> obtenerTodos();
    Optional<Empleado> buscarPorId(String idEmpleado);
    Optional<Empleado> buscarPorNombreUsuario(String nombreUsuario);
    void agregar(Empleado empleado) throws EmpleadoDuplicadoException;
    void actualizar(Empleado empleado) throws EmpleadoNoEncontradoException;
    void eliminar(String idEmpleado) throws EmpleadoNoEncontradoException;
    
}
