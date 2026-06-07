package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Valores validos para el tipo de cuenta bancaria. Sustituye al antiguo
 * enum TipoCuenta.
 */
public final class CatalogoTipoCuenta {

    public static final String AHORROS = "AHORROS";
    public static final String CORRIENTE = "CORRIENTE";
    public static final String EMPRESARIAL = "EMPRESARIAL";

    private CatalogoTipoCuenta() {
    }

    public static List<String> valores() {
        List<String> valores = new ArrayList<String>();
        valores.add(AHORROS);
        valores.add(CORRIENTE);
        valores.add(EMPRESARIAL);
        return valores;
    }
}
