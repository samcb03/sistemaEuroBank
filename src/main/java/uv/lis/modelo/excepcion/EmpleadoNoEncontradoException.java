package uv.lis.modelo.excepcion;

public class EmpleadoNoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmpleadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}