package uv.lis.modelo;


public class CuentaAhorros extends CuentaBancaria {
    private double tasaInteres;

    public CuentaAhorros(String numeroCuenta, double saldoInicial, Cliente cliente, String idSucursal, double tasaInteres) {
        super(numeroCuenta, saldoInicial, cliente);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= getSaldo()) {
            setSaldo(getSaldo() - monto);
            return true;
        }
        return false;
    }

    @Override
    public double getFondosDisponibles() {
        return getSaldo();
    }

    public void aplicarInteres() {
        setSaldo(getSaldo() + (getSaldo() * tasaInteres / 100));
    }

    public double getTasaInteres() { 
        return tasaInteres; 
    }
    public void setTasaInteres(double tasaInteres) { 
        this.tasaInteres = tasaInteres; 
    }
}