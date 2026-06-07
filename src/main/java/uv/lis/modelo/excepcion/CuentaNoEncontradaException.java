package uv.lis.modelo.excepcion;

public class CuentaNoEncontradaException extends Exception {
    private static final long serialVersionUID = 1L;

    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}