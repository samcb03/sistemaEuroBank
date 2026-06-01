package uv.lis.modelo.excepcion;

public class PersistenciaSucursalException extends RuntimeException {

    public PersistenciaSucursalException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
