package uv.lis.controlador;

import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.CuentaEmpresarial;
import uv.lis.modelo.excepcion.CuentaDuplicadaException;
import uv.lis.modelo.excepcion.CuentaNoEncontradaException;
import uv.lis.modelo.excepcion.SaldoInsuficienteException;
import uv.lis.vista.VistaCuenta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaControlador {

    private final List<CuentaBancaria> cuentas = new ArrayList<>();
    private VistaCuenta vista;

    public void iniciar(VistaCuenta vista) {
        this.vista = vista;
        vista.refrescarTabla(obtenerTodasLasCuentas());

        vista.obtenerBotonAgregar().setOnAction(e -> solicitarAbrirCuenta());
        vista.obtenerBotonCerrar().setOnAction(e -> solicitarCerrarCuenta());
    }

    public void abrirCuenta(CuentaBancaria cuenta)
            throws CuentaDuplicadaException {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank()) {
            throw new IllegalArgumentException("El número de cuenta es obligatorio.");
        }
        if (existeCuenta(cuenta.getNumeroCuenta())) {
            throw new CuentaDuplicadaException(
                "Ya existe una cuenta con el número: " + cuenta.getNumeroCuenta());
        }
        cuentas.add(cuenta);
    }

    public void cerrarCuenta(String numeroCuenta)
            throws CuentaNoEncontradaException, SaldoInsuficienteException {
        CuentaBancaria cuenta = buscarCuentaOExcepcion(numeroCuenta);
        if (cuenta.getSaldo() != 0) {
            throw new SaldoInsuficienteException(
                "La cuenta " + numeroCuenta + " aún tiene saldo $"
                + cuenta.getSaldo() + ". Debe ser $0 para cerrarla.");
        }
        cuentas.remove(cuenta);
    }

    public void modificarLimiteCredito(String numeroCuenta, double nuevoLimite)
            throws CuentaNoEncontradaException {
        CuentaBancaria cuenta = buscarCuentaOExcepcion(numeroCuenta);
        if (!(cuenta instanceof CuentaEmpresarial)) {
            throw new IllegalArgumentException(
                "Solo las cuentas empresariales permiten modificar el límite de crédito.");
        }
        ((CuentaEmpresarial) cuenta).setLimiteCredito(nuevoLimite);
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

    private void solicitarAbrirCuenta() {
        vista.mostrarMensajeInformacion("Funcionalidad de apertura de cuenta pendiente.");
    }

    private void solicitarCerrarCuenta() {
        CuentaBancaria seleccionada = vista.obtenerCuentaSeleccionada();
        if (seleccionada == null) {
            vista.mostrarMensajeError("Seleccione una cuenta para cerrar.");
            return;
        }
        if (vista.confirmarCierre(seleccionada.getNumeroCuenta())) {
            try {
                cerrarCuenta(seleccionada.getNumeroCuenta());
                vista.refrescarTabla(obtenerTodasLasCuentas());
                vista.mostrarMensajeInformacion("Cuenta cerrada correctamente.");
            } catch (CuentaNoEncontradaException | SaldoInsuficienteException ex) {
                vista.mostrarMensajeError(ex.getMessage());
            }
        }
    }

    private CuentaBancaria buscarCuentaOExcepcion(String numeroCuenta)
            throws CuentaNoEncontradaException {
        return buscarCuenta(numeroCuenta)
            .orElseThrow(() -> new CuentaNoEncontradaException(
                "No se encontró ninguna cuenta con el número: " + numeroCuenta));
    }
}