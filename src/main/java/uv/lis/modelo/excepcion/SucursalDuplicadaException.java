package uv.lis.modelo.excepcion;

public class SucursalDuplicadaException extends Exception {

    private static final long serialVersionUID = 1L;

    public SucursalDuplicadaException(String mensaje) {
        super(mensaje);
    }
}