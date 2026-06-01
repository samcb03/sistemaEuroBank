package uv.lis.modelo.excepcion;

public class SucursalNoEncontradaException extends Exception {

    private static final long serialVersionUID = 1L;

    public SucursalNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}