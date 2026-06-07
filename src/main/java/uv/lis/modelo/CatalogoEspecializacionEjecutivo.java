package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Valores validos para la especializacion de un ejecutivo de cuenta.
 * Sustituye al antiguo enum EspecializacionEjecutivo.
 */
public final class CatalogoEspecializacionEjecutivo {

    public static final String PYMES = "PYMES";
    public static final String CORPORATIVO = "CORPORATIVO";

    private CatalogoEspecializacionEjecutivo() {
    }

    public static List<String> valores() {
        List<String> valores = new ArrayList<String>();
        valores.add(PYMES);
        valores.add(CORPORATIVO);
        return valores;
    }
}
