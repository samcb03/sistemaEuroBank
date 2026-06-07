package uv.lis.modelo;

public abstract class CuentaBancaria {

    private String numeroCuenta;
    private double saldo;
    private Cliente cliente;
    private String idSucursal;

    public CuentaBancaria(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.cliente = cliente;
        this.idSucursal = "";
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public abstract boolean retirar(double monto);

    public abstract double getFondosDisponibles();

    /**
     * Devuelve el subtipo concreto de la cuenta. Sustituye a las
     * comprobaciones instanceof en la capa de persistencia.
     */
    public abstract String obtenerTipoCuenta();

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado;
        if (this == obj) {
            resultado = true;
        } else if (!(obj instanceof CuentaBancaria)) {
            resultado = false;
        } else {
            CuentaBancaria otra = (CuentaBancaria) obj;
            resultado = numeroCuenta != null && numeroCuenta.equals(otra.numeroCuenta);
        }
        return resultado;
    }

    @Override
    public int hashCode() {
        return numeroCuenta != null ? numeroCuenta.hashCode() : 0;
    }
}