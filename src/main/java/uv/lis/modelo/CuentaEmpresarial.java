package uv.lis.modelo;


public class CuentaEmpresarial extends CuentaBancaria {

    private String razonSocial;
    private double limiteCredito;

    public CuentaEmpresarial(String numeroCuenta, double saldoInicial,Cliente cliente) {
        super(numeroCuenta,saldoInicial,cliente);
        this.razonSocial = "";
        this.limiteCredito = 0.0;
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= getFondosDisponibles()) {
            setSaldo(getSaldo() - monto);
            return true;
        }
        return false;
    }

    public double getLimiteCredito() { 
        return limiteCredito; 
    }   
    
    public void setLimiteCredito(double limiteCredito) { 
        this.limiteCredito = limiteCredito; 
    }

    @Override
    public double getFondosDisponibles() {
        return getSaldo() + getLimiteCredito();
    }

    public String getRazonSocial() { 
        return razonSocial; 
    }
    public void setRazonSocial(String razonSocial) { 
        this.razonSocial = razonSocial; 
    }
}