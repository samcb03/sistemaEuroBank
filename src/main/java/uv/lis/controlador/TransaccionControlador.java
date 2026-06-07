package uv.lis.controlador;

import uv.lis.modelo.excepcion.SaldoInsuficienteException;
import uv.lis.modelo.excepcion.TransaccionFallidaException;
import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.Transaccion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransaccionControlador {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final List<Transaccion> transacciones;
    private final List<CuentaBancaria> cuentas;

    public TransaccionControlador(List<CuentaBancaria> cuentas) {
        this.cuentas = cuentas;
        this.transacciones = new ArrayList<>();
    }

    public List<String> obtenerTiposDisponibles() {
        List<String> tipos = new ArrayList<>();
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
        Transaccion t = new Transaccion(
                Transaccion.DEPOSITO, monto,
                LocalDateTime.now().format(FORMATTER),
                numeroCuenta, null, idSucursal);
        registrar(t);
        return t;
    }

    public Transaccion registrarRetiro(String numeroCuenta, double monto, String idSucursal)
            throws TransaccionFallidaException, SaldoInsuficienteException {
        validarMonto(monto);
        CuentaBancaria cuenta = obtenerCuentaActiva(numeroCuenta);

        boolean exito = cuenta.retirar(monto);
        if (!exito) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente en la cuenta " + numeroCuenta
                    + ". Disponible: $" + cuenta.getFondosDisponibles()
                    + ", solicitado: $" + monto + ".");
        }

        Transaccion t = new Transaccion(
                Transaccion.RETIRO, monto,
                LocalDateTime.now().format(FORMATTER),
                numeroCuenta, null, idSucursal);
        registrar(t);
        return t;
    }
    
    public Transaccion registrarTransferencia(String cuentaOrigen, String cuentaDestino,
            double monto, String idSucursal)
            throws TransaccionFallidaException, SaldoInsuficienteException {
        validarMonto(monto);
        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new TransaccionFallidaException(
                    "La cuenta origen y destino no pueden ser la misma.");
        }

        CuentaBancaria origen  = obtenerCuentaActiva(cuentaOrigen);
        CuentaBancaria destino = obtenerCuentaActiva(cuentaDestino);

        boolean exito = origen.retirar(monto);
        if (!exito) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente en la cuenta origen " + cuentaOrigen
                    + ". Disponible: $" + origen.getFondosDisponibles()
                    + ", solicitado: $" + monto + ".");
        }

        destino.depositar(monto);

        Transaccion t = new Transaccion(
                Transaccion.TRANSFERENCIA, monto,
                LocalDateTime.now().format(FORMATTER),
                cuentaOrigen, cuentaDestino, idSucursal);
        registrar(t);
        return t;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        return transacciones.stream()
                .filter(t -> numeroCuenta.equals(t.getCuentaOrigen())
                          || numeroCuenta.equals(t.getCuentaDestino()))
                .collect(Collectors.toList());
    }

    private CuentaBancaria obtenerCuentaActiva(String numeroCuenta)
            throws TransaccionFallidaException {
        return cuentas.stream()
                .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
                .findFirst()
                .orElseThrow(() -> new TransaccionFallidaException(
                        "La cuenta " + numeroCuenta + " no existe."));
    }

    private void validarMonto(double monto) throws TransaccionFallidaException {
        if (monto <= 0) {
            throw new TransaccionFallidaException("El monto debe ser mayor a cero.");
        }
    }

    private void registrar(Transaccion transaccion) {
        transacciones.add(transaccion);
    }
}