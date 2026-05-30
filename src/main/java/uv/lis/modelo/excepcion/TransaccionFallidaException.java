package uv.lis.modelo.excepcion;

public class TransaccionFallidaException extends Exception {
    private static final long serialVersionUID = 1L;

    public TransaccionFallidaException(String mensaje) {
        super(mensaje);
    }

}
