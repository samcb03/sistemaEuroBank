package uv.lis.modelo.excepcion;

public class DatosSucursalInvalidosException extends Exception {

    private static final long serialVersionUID = 1L;

    public DatosSucursalInvalidosException(String mensaje) {
        super(mensaje);
    }
}