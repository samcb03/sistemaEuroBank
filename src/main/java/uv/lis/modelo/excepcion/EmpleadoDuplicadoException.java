package uv.lis.modelo.excepcion;

public class EmpleadoDuplicadoException extends Exception {

    private static final long serialVersionUID = 1L;

    public EmpleadoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}

