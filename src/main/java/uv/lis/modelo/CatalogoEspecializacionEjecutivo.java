package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

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
