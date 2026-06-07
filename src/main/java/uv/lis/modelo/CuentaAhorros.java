package uv.lis.modelo;

public class CuentaAhorros extends CuentaBancaria {

    private double tasaInteres;

    public CuentaAhorros(String numeroCuenta, double saldoInicial, Cliente cliente,
                         String idSucursal, double tasaInteres) {
        super(numeroCuenta, saldoInicial, cliente);
        setIdSucursal(idSucursal);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public boolean retirar(double monto) {
        boolean realizado = false;
        if (monto > 0 && monto <= getSaldo()) {
            setSaldo(getSaldo() - monto);
            realizado = true;
        }
        return realizado;
    }

    @Override
    public double getFondosDisponibles() {
        return getSaldo();
    }

    @Override
    public TipoCuenta obtenerTipoCuenta() {
        return TipoCuenta.AHORROS;
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