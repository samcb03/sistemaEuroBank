package uv.lis.modelo.excepcion;

public class DatosEmpleadoInvalidosException extends Exception {

    private static final long serialVersionUID = 1L;

    public DatosEmpleadoInvalidosException(String mensaje) {
        super(mensaje);
    }
}
