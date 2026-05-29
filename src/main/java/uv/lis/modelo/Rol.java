package uv.lis.modelo;

public enum Rol {

    ADMINISTRADOR("Administrador"),
    GERENTE("Gerente"),
    CAJERO("Cajero"),
    EJECUTIVO("Ejecutivo");

    private final String nombreVisible;

    Rol(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String obtenerNombreVisible() {
        return nombreVisible;
    }
}