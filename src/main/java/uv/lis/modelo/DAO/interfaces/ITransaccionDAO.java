package uv.lis.modelo.DAO.interfaces;

import java.util.List;

import uv.lis.modelo.Transaccion;

public interface ITransaccionDAO {
    List<Transaccion> obtenerPorCuenta(String numeroCuenta);
    List<Transaccion> obtenerTodas();
    void agregar(Transaccion transaccion);
}
