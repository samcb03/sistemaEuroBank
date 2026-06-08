package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

public final class CatalogoNivelAccesoGerente {

    public static final String SUCURSAL = "SUCURSAL";
    public static final String REGIONAL = "REGIONAL";
    public static final String NACIONAL = "NACIONAL";

    private CatalogoNivelAccesoGerente() {
    }

    public static List<String> valores() {
        List<String> valores = new ArrayList<String>();
        valores.add(SUCURSAL);
        valores.add(REGIONAL);
        valores.add(NACIONAL);
        return valores;
    }
}
