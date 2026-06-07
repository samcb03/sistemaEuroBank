package uv.lis.modelo;

public class CuentaEmpresarial extends CuentaBancaria {

    private String razonSocial;
    private double limiteCredito;

    public CuentaEmpresarial(String numeroCuenta, double saldoInicial, Cliente cliente) {
        super(numeroCuenta, saldoInicial, cliente);
        this.razonSocial = "";
        this.limiteCredito = 0.0;
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
        return CatalogoTipoCuenta.EMPRESARIAL;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
}