package uv.lis.modelo.DAO.interfaces;

import java.util.List;
import java.util.Optional;

import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.excepcion.CuentaDuplicadaException;

public interface ICuentaDAO {
    List<CuentaBancaria> obtenerTodas();
    Optional<CuentaBancaria> buscarPorNumero(String numeroCuenta);
    boolean existe(String numeroCuenta);
    void agregar(CuentaBancaria cuenta) throws CuentaDuplicadaException;
    void actualizar(CuentaBancaria cuenta);
    void eliminar(String numeroCuenta);
}
