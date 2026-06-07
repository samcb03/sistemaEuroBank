package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Valores validos para el genero de un empleado. Sustituye al antiguo
 * enum Genero por constantes de texto que coinciden con el catalogo de
 * la base de datos.
 */
public final class CatalogoGenero {

    public static final String MASCULINO = "MASCULINO";
    public static final String FEMENINO = "FEMENINO";
    public static final String OTRO = "OTRO";

    private CatalogoGenero() {
    }

    public static List<String> valores() {
        List<String> valores = new ArrayList<String>();
        valores.add(MASCULINO);
        valores.add(FEMENINO);
        valores.add(OTRO);
        return valores;
    }
}
