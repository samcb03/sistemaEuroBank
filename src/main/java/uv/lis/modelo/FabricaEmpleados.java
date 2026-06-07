package uv.lis.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import uv.lis.modelo.excepcion.DatosEmpleadoInvalidosException;

public class FabricaEmpleados {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String SEPARADOR_ERRORES = "\n";

    public Empleado crearDesdeDatos(DatosFormularioEmpleado datos)
            throws DatosEmpleadoInvalidosException {
        List<String> errores = recolectarErrores(datos);
        if (!errores.isEmpty()) {
            throw new DatosEmpleadoInvalidosException(unirErrores(errores));
        }
        Empleado empleadoConstruido = construirEmpleadoValido(datos);
        return empleadoConstruido;
    }

    private List<String> recolectarErrores(DatosFormularioEmpleado datos) {
        List<String> errores = new ArrayList<String>();
        agregarErroresComunes(datos, errores);
        agregarErroresEspecificos(datos, errores);
        return errores;
    }

    private void agregarErroresComunes(DatosFormularioEmpleado datos, List<String> errores) {
        if (esTextoVacio(datos.getIdEmpleado())) {
            errores.add("El ID del empleado es obligatorio.");
        }
        if (esTextoVacio(datos.getNombreCompleto())) {
            errores.add("El nombre completo es obligatorio.");
        }
        if (esTextoVacio(datos.getDireccion())) {
            errores.add("La direccion es obligatoria.");
        }
        if (esTextoVacio(datos.getNombreUsuario())) {
            errores.add("El nombre de usuario es obligatorio.");
        }
        if (esTextoVacio(datos.getContrasenia())) {
            errores.add("La contrasena es obligatoria.");
        }
        if (datos.getGenero() == null) {
            errores.add("Debe seleccionar un genero.");
        }
        if (datos.getRol() == null) {
            errores.add("Debe seleccionar un tipo de empleado.");
        }
        agregarErroresFechaNacimiento(datos, errores);
        agregarErroresSalario(datos, errores);
    }

    private void agregarErroresFechaNacimiento(DatosFormularioEmpleado datos, List<String> errores) {
        LocalDate fechaNacimiento = datos.getFechaNacimiento();
        if (fechaNacimiento == null) {
            errores.add("Debe seleccionar la fecha de nacimiento.");
        } else if (fechaNacimiento.isAfter(LocalDate.now())) {
            errores.add("La fecha de nacimiento no puede ser futura.");
        }
    }

    private void agregarErroresSalario(DatosFormularioEmpleado datos, List<String> errores) {
        if (!esDecimalNoNegativoValido(datos.getSalarioTexto())) {
            errores.add("El salario debe ser un numero mayor o igual a cero.");
        }
    }

    private void agregarErroresEspecificos(DatosFormularioEmpleado datos, List<String> errores) {
        String rol = datos.getRol();
        if (CatalogoRol.CAJERO.equals(rol)) {
            agregarErroresCajero(datos, errores);
        } else if (CatalogoRol.EJECUTIVO.equals(rol)) {
            agregarErroresEjecutivo(datos, errores);
        } else if (CatalogoRol.GERENTE.equals(rol)) {
            agregarErroresGerente(datos, errores);
        }
    }

    private void agregarErroresCajero(DatosFormularioEmpleado datos, List<String> errores) {
        boolean inicioValido = esHoraValida(datos.getHoraInicioTurnoTexto());
        boolean finValido = esHoraValida(datos.getHoraFinTurnoTexto());
        if (!inicioValido) {
            errores.add("La hora de inicio del turno debe tener formato HH:mm.");
        }
        if (!finValido) {
            errores.add("La hora de fin del turno debe tener formato HH:mm.");
        }
        if (inicioValido && finValido && !inicioEsAnteriorAFin(datos)) {
            errores.add("La hora de inicio debe ser anterior a la hora de fin.");
        }
        if (!esEnteroPositivoValido(datos.getNumeroVentanillaTexto())) {
            errores.add("El numero de ventanilla debe ser un entero positivo.");
        }
    }

    private void agregarErroresEjecutivo(DatosFormularioEmpleado datos, List<String> errores) {
        if (!esEnteroNoNegativoValido(datos.getNumeroClientesTexto())) {
            errores.add("El numero de clientes asignados debe ser un entero mayor o igual a cero.");
        }
        if (datos.getEspecializacion() == null) {
            errores.add("Debe seleccionar la especializacion del ejecutivo.");
        }
    }

    private void agregarErroresGerente(DatosFormularioEmpleado datos, List<String> errores) {
        if (datos.getNivelAcceso() == null) {
            errores.add("Debe seleccionar el nivel de acceso del gerente.");
        }
        if (!esEnteroNoNegativoValido(datos.getAniosExperienciaTexto())) {
            errores.add("Los anios de experiencia deben ser un entero mayor o igual a cero.");
        }
    }

    private boolean inicioEsAnteriorAFin(DatosFormularioEmpleado datos) {
        LocalTime inicio = LocalTime.parse(datos.getHoraInicioTurnoTexto().trim(), FORMATO_HORA);
        LocalTime fin = LocalTime.parse(datos.getHoraFinTurnoTexto().trim(), FORMATO_HORA);
        boolean anterior = inicio.isBefore(fin);
        return anterior;
    }

    private Empleado construirEmpleadoValido(DatosFormularioEmpleado datos) {
        Empleado empleado;
        String rol = datos.getRol();
        if (CatalogoRol.ADMINISTRADOR.equals(rol)) {
            empleado = construirAdministrador(datos);
        } else if (CatalogoRol.GERENTE.equals(rol)) {
            empleado = construirGerente(datos);
        } else if (CatalogoRol.CAJERO.equals(rol)) {
            empleado = construirCajero(datos);
        } else {
            empleado = construirEjecutivo(datos);
        }
        return empleado;
    }

    private Administrador construirAdministrador(DatosFormularioEmpleado datos) {
        Administrador administrador = new Administrador(
                datos.getIdEmpleado().trim(),
                datos.getNombreCompleto().trim(),
                datos.getDireccion().trim(),
                datos.getFechaNacimiento(),
                datos.getGenero(),
                Double.parseDouble(datos.getSalarioTexto().trim()),
                datos.getNombreUsuario().trim(),
                datos.getContrasenia());
        return administrador;
    }

    private Gerente construirGerente(DatosFormularioEmpleado datos) {
        Gerente gerente = new Gerente(
                datos.getIdEmpleado().trim(),
                datos.getNombreCompleto().trim(),
                datos.getDireccion().trim(),
                datos.getFechaNacimiento(),
                datos.getGenero(),
                Double.parseDouble(datos.getSalarioTexto().trim()),
                datos.getNombreUsuario().trim(),
                datos.getContrasenia(),
                datos.getNivelAcceso(),
                Integer.parseInt(datos.getAniosExperienciaTexto().trim()));
        return gerente;
    }

    private Cajero construirCajero(DatosFormularioEmpleado datos) {
        Cajero cajero = new Cajero(
                datos.getIdEmpleado().trim(),
                datos.getNombreCompleto().trim(),
                datos.getDireccion().trim(),
                datos.getFechaNacimiento(),
                datos.getGenero(),
                Double.parseDouble(datos.getSalarioTexto().trim()),
                datos.getNombreUsuario().trim(),
                datos.getContrasenia(),
                LocalTime.parse(datos.getHoraInicioTurnoTexto().trim(), FORMATO_HORA),
                LocalTime.parse(datos.getHoraFinTurnoTexto().trim(), FORMATO_HORA),
                Integer.parseInt(datos.getNumeroVentanillaTexto().trim()));
        return cajero;
    }

    private EjecutivoCuenta construirEjecutivo(DatosFormularioEmpleado datos) {
        EjecutivoCuenta ejecutivo = new EjecutivoCuenta(
                datos.getIdEmpleado().trim(),
                datos.getNombreCompleto().trim(),
                datos.getDireccion().trim(),
                datos.getFechaNacimiento(),
                datos.getGenero(),
                Double.parseDouble(datos.getSalarioTexto().trim()),
                datos.getNombreUsuario().trim(),
                datos.getContrasenia(),
                Integer.parseInt(datos.getNumeroClientesTexto().trim()),
                datos.getEspecializacion());
        return ejecutivo;
    }

    private String unirErrores(List<String> errores) {
        String mensaje = String.join(SEPARADOR_ERRORES, errores);
        return mensaje;
    }

    private boolean esTextoVacio(String texto) {
        boolean vacio = texto == null || texto.trim().isEmpty();
        return vacio;
    }

    private boolean esDecimalNoNegativoValido(String texto) {
        boolean valido = false;
        if (!esTextoVacio(texto)) {
            valido = intentarConvertirDecimalNoNegativo(texto.trim());
        }
        return valido;
    }

    private boolean intentarConvertirDecimalNoNegativo(String texto) {
        boolean valido;
        try {
            double valor = Double.parseDouble(texto);
            valido = valor >= 0;
        } catch (NumberFormatException excepcion) {
            valido = false;
        }
        return valido;
    }

    private boolean esEnteroNoNegativoValido(String texto) {
        boolean valido = esEnteroValido(texto) && Integer.parseInt(texto.trim()) >= 0;
        return valido;
    }

    private boolean esEnteroPositivoValido(String texto) {
        boolean valido = esEnteroValido(texto) && Integer.parseInt(texto.trim()) > 0;
        return valido;
    }

    private boolean esEnteroValido(String texto) {
        boolean valido = false;
        if (!esTextoVacio(texto)) {
            valido = intentarConvertirEntero(texto.trim());
        }
        return valido;
    }

    private boolean intentarConvertirEntero(String texto) {
        boolean valido;
        try {
            Integer.parseInt(texto);
            valido = true;
        } catch (NumberFormatException excepcion) {
            valido = false;
        }
        return valido;
    }

    private boolean esHoraValida(String texto) {
        boolean valida = false;
        if (!esTextoVacio(texto)) {
            valida = intentarConvertirHora(texto.trim());
        }
        return valida;
    }

    private boolean intentarConvertirHora(String texto) {
        boolean valida;
        try {
            LocalTime.parse(texto, FORMATO_HORA);
            valida = true;
        } catch (RuntimeException excepcion) {
            valida = false;
        }
        return valida;
    }
}