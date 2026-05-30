package uv.lis.modelo;

public class CuentaCorriente extends CuentaBancaria {

    private double limiteCredito;

    public CuentaCorriente(String numeroCuenta, double saldoInicial, Cliente cliente, String idSucursal, double limiteCredito) {
        super(numeroCuenta, saldoInicial, cliente);
        this.limiteCredito = limiteCredito;
    }

    @Override
    public boolean retirar(double monto) {
        if (monto > 0 && monto <= getFondosDisponibles()) {
            setSaldo(getSaldo() - monto);
            return true;
        }
        return false;
    }

    @Override
    public double getFondosDisponibles() {
        return getSaldo() + this.limiteCredito;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
}