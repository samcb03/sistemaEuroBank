package uv.lis.modelo;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidadorCampos {

    private static final Pattern PATRON_CORREO
            = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PATRON_TELEFONO
            = Pattern.compile("^\\d{10}$");

    private static final Pattern PATRON_SOLO_LETRAS
            = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\\s]+$");

    private static final Pattern PATRON_RFC_PERSONA_FISICA
            = Pattern.compile("^[A-ZÑ&]{4}\\d{6}[A-Z0-9]{3}$");

    private static final Pattern PATRON_RFC_PERSONA_MORAL
            = Pattern.compile("^[A-ZÑ&]{3}\\d{6}[A-Z0-9]{3}$");

    private static final Pattern PATRON_CURP
            = Pattern.compile("^[A-Z][AEIOUX][A-Z]{2}\\d{2}"
                    + "(0[1-9]|1[0-2])"
                    + "(0[1-9]|[12]\\d|3[01])"
                    + "[HM]"
                    + "(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)"
                    + "[B-DF-HJ-NP-TV-Z]{3}"
                    + "[A-Z0-9]"
                    + "\\d$");

    private static final String CARACTERES_CURP
            = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

    public static void validarObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }
    }

    public static void validarSoloLetras(String valor, String nombreCampo) {
        validarObligatorio(valor, nombreCampo);

        if (!PATRON_SOLO_LETRAS.matcher(valor.trim()).matches()) {
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + " solo debe contener letras.");
        }
    }

    public static void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
    }

    public static void validarCorreo(String correo) {
        validarObligatorio(correo, "correo");

        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
    }

    public static void validarTelefono(String telefono) {
        validarObligatorio(telefono, "teléfono");

        if (!PATRON_TELEFONO.matcher(telefono.trim()).matches()) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos.");
        }
    }

    public static void validarRfcCurp(String rfcCurp) {
        validarObligatorio(rfcCurp, "RFC/CURP");

        String valor = rfcCurp.trim().toUpperCase();

        if (valor.length() == 18) {
            validarCurp(valor);
        } else if (valor.length() == 12 || valor.length() == 13) {
            validarRfc(valor);
        } else {
            throw new IllegalArgumentException(
                    "Debe ingresar un RFC de 12 o 13 caracteres, o una CURP de 18 caracteres.");
        }
    }

    public static void validarRfc(String rfc) {
        validarObligatorio(rfc, "RFC");

        String rfcNormalizado = rfc.trim().toUpperCase();

        boolean esPersonaFisica
                = PATRON_RFC_PERSONA_FISICA.matcher(rfcNormalizado).matches();

        boolean esPersonaMoral
                = PATRON_RFC_PERSONA_MORAL.matcher(rfcNormalizado).matches();

        if (!esPersonaFisica && !esPersonaMoral) {
            throw new IllegalArgumentException(
                    "El RFC debe tener 13 caracteres para persona física o 12 para persona moral, "
                    + "incluyendo fecha AAMMDD y homoclave de 3 caracteres.");
        }

        validarFechaRfc(rfcNormalizado);
    }

    public static void validarCurp(String curp) {
        validarObligatorio(curp, "CURP");

        String curpNormalizada = curp.trim().toUpperCase();

        if (!PATRON_CURP.matcher(curpNormalizada).matches()) {
            throw new IllegalArgumentException(
                    "La CURP debe tener exactamente 18 caracteres y cumplir el formato oficial.");
        }

        validarFechaCurp(curpNormalizada);

        if (!digitoVerificadorCurpValido(curpNormalizada)) {
            throw new IllegalArgumentException(
                    "La CURP no tiene un dígito verificador válido.");
        }
    }

    private static void validarFechaRfc(String rfc) {
        int inicioFecha = rfc.length() == 13 ? 4 : 3;

        int anio = Integer.parseInt(rfc.substring(inicioFecha, inicioFecha + 2));
        int mes = Integer.parseInt(rfc.substring(inicioFecha + 2, inicioFecha + 4));
        int dia = Integer.parseInt(rfc.substring(inicioFecha + 4, inicioFecha + 6));

        int anioActualDosDigitos = LocalDate.now().getYear() % 100;
        int siglo = anio <= anioActualDosDigitos ? 2000 : 1900;
        int anioCompleto = siglo + anio;

        try {
            LocalDate fecha = LocalDate.of(anioCompleto, mes, dia);

            if (fecha.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha del RFC no puede ser futura.");
            }

        } catch (java.time.DateTimeException ex) {
            throw new IllegalArgumentException("La fecha contenida en el RFC no es válida.");
        }
    }

    private static void validarFechaCurp(String curp) {
        int anio = Integer.parseInt(curp.substring(4, 6));
        int mes = Integer.parseInt(curp.substring(6, 8));
        int dia = Integer.parseInt(curp.substring(8, 10));

        char homoclave = curp.charAt(16);
        int siglo = Character.isDigit(homoclave) ? 1900 : 2000;
        int anioCompleto = siglo + anio;

        try {
            LocalDate fecha = LocalDate.of(anioCompleto, mes, dia);

            if (fecha.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de la CURP no puede ser futura.");
            }

        } catch (java.time.DateTimeException ex) {
            throw new IllegalArgumentException("La fecha contenida en la CURP no es válida.");
        }
    }

    private static boolean digitoVerificadorCurpValido(String curp) {
        int suma = 0;

        for (int i = 0; i < 17; i++) {
            int valor = CARACTERES_CURP.indexOf(curp.charAt(i));

            if (valor == -1) {
                return false;
            }

            suma += valor * (18 - i);
        }

        int residuo = suma % 10;
        int digitoCalculado = residuo == 0 ? 0 : 10 - residuo;
        int digitoIngresado = Character.getNumericValue(curp.charAt(17));

        return digitoCalculado == digitoIngresado;
    }
}
