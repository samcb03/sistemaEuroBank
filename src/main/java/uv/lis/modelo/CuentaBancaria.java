package uv.lis.modelo;

public abstract class CuentaBancaria {

    private String numeroCuenta;
    private double saldo;
    private Cliente cliente;


    public CuentaBancaria(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.cliente = cliente;
    }


    public void depositar(double monto) {
        saldo += monto;
    }

    public abstract boolean retirar(double monto);


    public abstract double getFondosDisponibles();


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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) obj;
        return numeroCuenta != null && numeroCuenta.equals(other.numeroCuenta);
    }

    @Override
    public int hashCode() {
        return numeroCuenta != null ? numeroCuenta.hashCode() : 0;
    }
}