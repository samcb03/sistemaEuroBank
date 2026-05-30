package uv.lis.modelo;

import java.util.UUID;

public class Transaccion {
    public static final String DEPOSITO = "Depósito";
    public static final String RETIRO = "Retiro";
    public static final String TRANSFERENCIA = "Transferencia";

    private String idTransaccion;
    private String tipo;
    private double monto;
    private String fechaHora;
    private String cuentaOrigen;
    private String cuentaDestino;
    private String idSucursal;

    public Transaccion() {}

    public Transaccion(String tipo, double monto, String fechaHora, String cuentaOrigen, String cuentaDestino, String idSucursal) {
        this.idTransaccion = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.idSucursal = idSucursal;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(String idTransaccion) { this.idTransaccion = idTransaccion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public String getIdSucursal() { return idSucursal; }
    public void setIdSucursal(String idSucursal) { this.idSucursal = idSucursal; }
}