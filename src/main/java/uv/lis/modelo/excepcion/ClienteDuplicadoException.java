package uv.lis.modelo.excepcion;

public class ClienteDuplicadoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}