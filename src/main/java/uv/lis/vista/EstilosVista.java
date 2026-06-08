package uv.lis.vista;

final class EstilosVista {

    static final String FONDO_VENTANA =
        "-fx-background-color: linear-gradient(to bottom, #eef2f6, #d6e0ea);";

    static final String TITULO =
        "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1f3a5f;";

    static final String SUBTITULO =
        "-fx-font-size: 13px; -fx-text-fill: #5a6b7b;";

    static final String TABLA =
        "-fx-background-color: white; -fx-background-radius: 6; "
        + "-fx-border-radius: 6; -fx-border-color: #c2ccd6;";

    static final String CAMPO =
        "-fx-background-radius: 5; -fx-border-radius: 5; "
        + "-fx-border-color: #c2ccd6; -fx-padding: 6;";

    private static final String BASE_BOTON =
        "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; "
        + "-fx-padding: 8 18 8 18; -fx-cursor: hand;";

    static final String BOTON_PRIMARIO =
        BASE_BOTON + "-fx-background-color: #2e6da4;";

    static final String BOTON_SECUNDARIO =
        BASE_BOTON + "-fx-background-color: #6c8ea8;";

    static final String BOTON_EXITO =
        BASE_BOTON + "-fx-background-color: #2e8b57;";

    static final String BOTON_PELIGRO =
        BASE_BOTON + "-fx-background-color: #c0392b;";

    private EstilosVista() {
    }
}

