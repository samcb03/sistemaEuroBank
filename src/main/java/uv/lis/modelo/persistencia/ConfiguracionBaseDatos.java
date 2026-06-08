package uv.lis.modelo.persistencia;

public final class ConfiguracionBaseDatos {

    public static final String URL =
        "jdbc:mysql://localhost:3306/eurobank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String USUARIO = "adminBanco";
    public static final String CONTRASENIA = "euro_bank521";

    private ConfiguracionBaseDatos() {
    }
}