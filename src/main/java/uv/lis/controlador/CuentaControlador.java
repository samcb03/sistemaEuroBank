package uv.lis.controlador;

import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.CuentaEmpresarial;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaControlador {

    private final List<CuentaBancaria> cuentas = null;

    public void abrirCuenta(CuentaBancaria cuenta) throws IllegalArgumentException {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank()) {
            throw new IllegalArgumentException("El número de cuenta es obligatorio.");
        }
        if (existeCuenta(cuenta.getNumeroCuenta())) {
            throw new IllegalArgumentException("La cuenta ya existe: " + cuenta.getNumeroCuenta());
        }
        cuentas.add(cuenta);
    }

    public void modificarLimiteCredito(String numeroCuenta, double nuevoLimite) throws IllegalArgumentException {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
        
        if (cuenta instanceof CuentaEmpresarial) {
            ((CuentaEmpresarial) cuenta).setLimiteCredito(nuevoLimite);
        } else {
            throw new IllegalArgumentException("Solo las cuentas empresariales permiten modificar el límite.");
        }
    }

    public void cerrarCuenta(String numeroCuenta) throws IllegalStateException {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalStateException("Cuenta no encontrada: " + numeroCuenta));
        
        if (cuenta.getSaldo() != 0) {
            throw new IllegalStateException("El saldo debe ser cero para cerrar la cuenta.");
        }
    }

    public Optional<CuentaBancaria> buscarCuenta(String numeroCuenta) {
        return cuentas.stream()
                .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
                .findFirst();
    }

    public boolean existeCuenta(String numeroCuenta) {
        return cuentas.stream()
                .anyMatch(c -> c.getNumeroCuenta().equals(numeroCuenta));
    }

    public List<CuentaBancaria> obtenerTodasLasCuentas() {
        return new ArrayList<>(cuentas);
    }

    public void actualizarSaldo(String numeroCuenta, double nuevoSaldo) {
        buscarCuenta(numeroCuenta).ifPresent(cuenta -> {
            cuenta.setSaldo(nuevoSaldo);
        });
    }

}