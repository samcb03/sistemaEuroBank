package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import uv.lis.modelo.excepcion.DatosSucursalInvalidosException;

public class FabricaSucursales {

    private static final String SEPARADOR_ERRORES = "\n";
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[0-9+\\-\\s]{7,20}$");

    public Sucursal crearDesdeDatos(DatosFormularioSucursal datos)
            throws DatosSucursalInvalidosException {
        List<String> errores = recolectarErrores(datos);
        if (!errores.isEmpty()) {
            throw new DatosSucursalInvalidosException(unirErrores(errores));
        }
        Sucursal sucursalConstruida = construirSucursalValida(datos);
        return sucursalConstruida;
    }

    private List<String> recolectarErrores(DatosFormularioSucursal datos) {
        List<String> errores = new ArrayList<String>();
        if (esTextoVacio(datos.getIdSucursal())) {
            errores.add("El numero de identificacion es obligatorio.");
        }
        if (esTextoVacio(datos.getNombre())) {
            errores.add("El nombre de la sucursal es obligatorio.");
        }
        if (esTextoVacio(datos.getDireccion())) {
            errores.add("La direccion es obligatoria.");
        }
        if (esTextoVacio(datos.getNombreGerente())) {
            errores.add("El nombre del gerente es obligatorio.");
        }
        if (esTextoVacio(datos.getPersonaContacto())) {
            errores.add("La persona de contacto es obligatoria.");
        }
        agregarErroresTelefono(datos, errores);
        agregarErroresCorreo(datos, errores);
        return errores;
    }

    private void agregarErroresTelefono(DatosFormularioSucursal datos, List<String> errores) {
        if (esTextoVacio(datos.getTelefono())) {
            errores.add("El telefono es obligatorio.");
        } else if (!PATRON_TELEFONO.matcher(datos.getTelefono().trim()).matches()) {
            errores.add("El telefono debe contener entre 7 y 20 digitos.");
        }
    }

    private void agregarErroresCorreo(DatosFormularioSucursal datos, List<String> errores) {
        if (esTextoVacio(datos.getCorreo())) {
            errores.add("El correo es obligatorio.");
        } else if (!PATRON_CORREO.matcher(datos.getCorreo().trim()).matches()) {
            errores.add("El correo no tiene un formato valido.");
        }
    }

    private Sucursal construirSucursalValida(DatosFormularioSucursal datos) {
        Sucursal sucursal = new Sucursal(
                datos.getIdSucursal().trim(),
                datos.getNombre().trim(),
                datos.getDireccion().trim(),
                datos.getTelefono().trim(),
                datos.getCorreo().trim(),
                datos.getNombreGerente().trim(),
                datos.getPersonaContacto().trim());
        return sucursal;
    }

    private String unirErrores(List<String> errores) {
        String mensaje = String.join(SEPARADOR_ERRORES, errores);
        return mensaje;
    }

    private boolean esTextoVacio(String texto) {
        boolean vacio = texto == null || texto.trim().isEmpty();
        return vacio;
    }
}