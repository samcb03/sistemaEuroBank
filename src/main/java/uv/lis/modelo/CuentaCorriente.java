package uv.lis.modelo;

public class CuentaCorriente extends CuentaBancaria {

    private double limiteCredito;

    public CuentaCorriente(String numeroCuenta, double saldoInicial, Cliente cliente,
                           String idSucursal, double limiteCredito) {
        super(numeroCuenta, saldoInicial, cliente);
        setIdSucursal(idSucursal);
        this.limiteCredito = limiteCredito;
    }

    @Override
    public boolean retirar(double monto) {
        boolean realizado = false;
        if (monto > 0 && monto <= getFondosDisponibles()) {
            setSaldo(getSaldo() - monto);
            realizado = true;
        }
        return realizado;
    }

    @Override
    public double getFondosDisponibles() {
        return getSaldo() + limiteCredito;
    }

    @Override
    public String obtenerTipoCuenta() {
        return CatalogoTipoCuenta.CORRIENTE;
    }

    @Override
    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
}