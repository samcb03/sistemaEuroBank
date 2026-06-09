package uv.lis.modelo;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class ValidadorCampos {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_RFC_CURP =
            Pattern.compile("^[A-Za-z0-9]{12,18}$");
    private static final Pattern PATRON_SOLO_LETRAS =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");

    private ValidadorCampos() {
    }

    public static void validarObligatorio(String valor, String nombreCampo) {
        if (estaVacio(valor)) {
            throw new IllegalArgumentException(
                    "El campo \"" + nombreCampo + "\" es obligatorio.");
        }
    }

    public static void validarSoloLetras(String valor, String nombreCampo) {
        validarObligatorio(valor, nombreCampo);
        if (!PATRON_SOLO_LETRAS.matcher(valor.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El campo \"" + nombreCampo + "\" solo admite letras y espacios.");
        }
    }

    public static void validarCorreo(String correo) {
        validarObligatorio(correo, "Correo");
        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El correo no tiene un formato válido (ejemplo: nombre@dominio.com).");
        }
    }

    public static void validarTelefono(String telefono) {
        validarObligatorio(telefono, "Teléfono");
        if (!PATRON_TELEFONO.matcher(telefono.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El teléfono debe contener exactamente 10 dígitos numéricos.");
        }
    }

    public static void validarRfcCurp(String rfcCurp) {
        validarObligatorio(rfcCurp, "RFC/CURP");
        if (!PATRON_RFC_CURP.matcher(rfcCurp.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El RFC/CURP debe tener entre 12 y 18 caracteres alfanuméricos, sin espacios.");
        }
    }

    public static void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento no puede ser una fecha futura.");
        }
    }

    private static boolean estaVacio(String valor) {
        boolean vacio = valor == null || valor.trim().isEmpty();
        return vacio;
    }
}