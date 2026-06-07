package uv.lis.controlador;

import uv.lis.modelo.excepcion.SaldoInsuficienteException;
import uv.lis.modelo.excepcion.TransaccionFallidaException;
import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.Transaccion;
import uv.lis.modelo.DAO.implementacion.CuentaDAO;
import uv.lis.modelo.DAO.implementacion.EmpleadoDAO;
import uv.lis.modelo.DAO.implementacion.TransaccionDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class TransaccionControlador {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final EmpleadoDAO repositorioEmpleados;
    private final CuentaDAO repositorioCuenta;
    private final TransaccionDAO repositorioTransaccion;

    public TransaccionControlador() {
        this.repositorioEmpleados = new EmpleadoDAO();
        this.repositorioCuenta = new CuentaDAO();
        this.repositorioTransaccion = new TransaccionDAO();
    }

    public TransaccionControlador(List<CuentaBancaria> cuentasIgnoradas) {
        this();
    }

    public List<String> obtenerTiposDisponibles() {
        List<String> tipos = new java.util.ArrayList<String>();
        tipos.add(Transaccion.DEPOSITO);
        tipos.add(Transaccion.RETIRO);
        tipos.add(Transaccion.TRANSFERENCIA);
        return tipos;
    }

    public Transaccion registrarDeposito(String numeroCuenta, double monto, String idSucursal)
            throws TransaccionFallidaException {
        validarMonto(monto);
        CuentaBancaria cuenta = obtenerCuentaActiva(numeroCuenta);
        cuenta.depositar(monto);
        repositorioCuenta.actualizar(cuenta);
        Transaccion transaccion = crearYRegistrar(
                Transaccion.DEPOSITO, monto, numeroCuenta, null, idSucursal);
        return transaccion;
    }

    public Transaccion registrarRetiro(String numeroCuenta, double monto, String idSucursal)
            throws TransaccionFallidaException, SaldoInsuficienteException {
        validarMonto(monto);
        CuentaBancaria cuenta = obtenerCuentaActiva(numeroCuenta);
        if (!cuenta.retirar(monto)) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente en la cuenta " + numeroCuenta
                    + ". Disponible: $" + cuenta.getFondosDisponibles()
                    + ", solicitado: $" + monto + ".");
        }
        repositorioCuenta.actualizar(cuenta);
        Transaccion transaccion = crearYRegistrar(
                Transaccion.RETIRO, monto, numeroCuenta, null, idSucursal);
        return transaccion;
    }

    public Transaccion registrarTransferencia(String cuentaOrigen, String cuentaDestino,
            double monto, String idSucursal)
            throws TransaccionFallidaException, SaldoInsuficienteException {
        validarMonto(monto);
        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new TransaccionFallidaException(
                    "La cuenta origen y destino no pueden ser la misma.");
        }
        CuentaBancaria origen = obtenerCuentaActiva(cuentaOrigen);
        CuentaBancaria destino = obtenerCuentaActiva(cuentaDestino);
        if (!origen.retirar(monto)) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente en la cuenta origen " + cuentaOrigen
                    + ". Disponible: $" + origen.getFondosDisponibles()
                    + ", solicitado: $" + monto + ".");
        }
        destino.depositar(monto);
        repositorioCuenta.actualizar(origen);
        repositorioCuenta.actualizar(destino);
        Transaccion transaccion = crearYRegistrar(
                Transaccion.TRANSFERENCIA, monto, cuentaOrigen, cuentaDestino, idSucursal);
        return transaccion;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        return repositorioTransaccion.obtenerPorCuenta(numeroCuenta);
    }

    private Transaccion crearYRegistrar(String tipo, double monto, String cuentaOrigen,
            String cuentaDestino, String idSucursal) {
        Transaccion transaccion = new Transaccion(
                tipo, monto, LocalDateTime.now().format(FORMATTER),
                cuentaOrigen, cuentaDestino, idSucursal);
        repositorioTransaccion.agregar(transaccion);
        return transaccion;
    }

    private CuentaBancaria obtenerCuentaActiva(String numeroCuenta)
            throws TransaccionFallidaException {
        Optional<CuentaBancaria> cuentaEncontrada = repositorioCuenta.buscarPorNumero(numeroCuenta);
        if (!cuentaEncontrada.isPresent()) {
            throw new TransaccionFallidaException(
                    "La cuenta " + numeroCuenta + " no existe.");
        }
        return cuentaEncontrada.get();
    }

    private void validarMonto(double monto) throws TransaccionFallidaException {
        if (monto <= 0) {
            throw new TransaccionFallidaException("El monto debe ser mayor a cero.");
        }
    }
}