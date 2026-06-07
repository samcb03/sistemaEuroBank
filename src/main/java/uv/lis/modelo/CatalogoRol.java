package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Valores validos para el rol de un empleado. Sustituye al antiguo enum
 * Rol por constantes de texto que coinciden con el catalogo de la base
 * de datos.
 */
public final class CatalogoRol {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String GERENTE = "GERENTE";
    public static final String CAJERO = "CAJERO";
    public static final String EJECUTIVO = "EJECUTIVO";

    private CatalogoRol() {
    }

    public static List<String> valores() {
        List<String> valores = new ArrayList<String>();
        valores.add(ADMINISTRADOR);
        valores.add(GERENTE);
        valores.add(CAJERO);
        valores.add(EJECUTIVO);
        return valores;
    }

    public static String nombreVisible(String rol) {
        String nombre;
        if (ADMINISTRADOR.equals(rol)) {
            nombre = "Administrador";
        } else if (GERENTE.equals(rol)) {
            nombre = "Gerente";
        } else if (CAJERO.equals(rol)) {
            nombre = "Cajero";
        } else if (EJECUTIVO.equals(rol)) {
            nombre = "Ejecutivo";
        } else {
            nombre = rol;
        }
        return nombre;
    }
}
