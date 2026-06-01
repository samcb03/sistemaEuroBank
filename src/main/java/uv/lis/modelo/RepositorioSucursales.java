package uv.lis.modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uv.lis.modelo.excepcion.PersistenciaSucursalException;
import uv.lis.modelo.excepcion.SucursalDuplicadaException;
import uv.lis.modelo.excepcion.SucursalNoEncontradaException;

public class RepositorioSucursales {

    private static final int INDICE_NO_ENCONTRADO = -1;
    private static final Path RUTA_ARCHIVO = Paths.get("datos", "sucursales.json");

    private final List<Sucursal> sucursales;

    public RepositorioSucursales() {
        this.sucursales = new ArrayList<Sucursal>();
        cargarDesdeArchivo();
    }

    public List<Sucursal> obtenerTodas() {
        return new ArrayList<Sucursal>(sucursales);
    }

    public Optional<Sucursal> buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        Optional<Sucursal> sucursalEncontrada = Optional.empty();
        for (Sucursal sucursal : sucursales) {
            if (sucursal.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                sucursalEncontrada = Optional.of(sucursal);
            }
        }
        return sucursalEncontrada;
    }

    public void agregar(Sucursal sucursal) throws SucursalDuplicadaException {
        validarSucursal(sucursal);
        if (existeNumeroIdentificacion(sucursal.getNumeroIdentificacion())) {
            throw new SucursalDuplicadaException(
                    "Ya existe una sucursal con el numero " + sucursal.getNumeroIdentificacion() + ".");
        }
        sucursales.add(sucursal);
        guardarEnArchivo();
    }

    public void actualizar(Sucursal sucursal) throws SucursalNoEncontradaException {
        validarSucursal(sucursal);
        int indice = obtenerIndicePorNumeroIdentificacion(sucursal.getNumeroIdentificacion());
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new SucursalNoEncontradaException(
                    "No se encontro la sucursal " + sucursal.getNumeroIdentificacion() + ".");
        }
        sucursales.set(indice, sucursal);
        guardarEnArchivo();
    }

    public void eliminar(String numeroIdentificacion) throws SucursalNoEncontradaException {
        int indice = obtenerIndicePorNumeroIdentificacion(numeroIdentificacion);
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new SucursalNoEncontradaException(
                    "No se encontro la sucursal " + numeroIdentificacion + ".");
        }
        sucursales.remove(indice);
        guardarEnArchivo();
    }

    private void validarSucursal(Sucursal sucursal) {
        if (estaVacio(sucursal.getNumeroIdentificacion())
                || estaVacio(sucursal.getNombre())
                || estaVacio(sucursal.getDireccion())
                || estaVacio(sucursal.getTelefono())
                || estaVacio(sucursal.getCorreo())
                || estaVacio(sucursal.getNombreGerente())
                || estaVacio(sucursal.getPersonaContacto())) {
            throw new IllegalArgumentException("Todos los campos de la sucursal son obligatorios.");
        }
        if (!sucursal.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo de la sucursal no tiene un formato valido.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private boolean existeNumeroIdentificacion(String numeroIdentificacion) {
        return obtenerIndicePorNumeroIdentificacion(numeroIdentificacion) != INDICE_NO_ENCONTRADO;
    }

    private int obtenerIndicePorNumeroIdentificacion(String numeroIdentificacion) {
        int indiceEncontrado = INDICE_NO_ENCONTRADO;
        for (int indice = 0; indice < sucursales.size(); indice++) {
            if (sucursales.get(indice).getNumeroIdentificacion().equals(numeroIdentificacion)) {
                indiceEncontrado = indice;
            }
        }
        return indiceEncontrado;
    }

    private void cargarDesdeArchivo() {
        try {
            if (Files.exists(RUTA_ARCHIVO)) {
                String contenido = Files.readString(RUTA_ARCHIVO, StandardCharsets.UTF_8);
                sucursales.clear();
                sucursales.addAll(convertirJsonASucursales(contenido));
            } else {
                cargarSucursalesIniciales();
                guardarEnArchivo();
            }
        } catch (IOException excepcion) {
            throw new PersistenciaSucursalException("No se pudo leer el archivo de sucursales.", excepcion);
        }
    }

    private void cargarSucursalesIniciales() {
        sucursales.add(new Sucursal("SUC-001", "Sucursal Central", "Av. Europa 100", "2281000001",
                "central@eurobank.com", "Maria Lopez", "Carlos Rivera"));
        sucursales.add(new Sucursal("SUC-002", "Sucursal Norte", "Calle Norte 45", "2281000002",
                "norte@eurobank.com", "Roberto Sanchez", "Laura Diaz"));
    }

    private void guardarEnArchivo() {
        try {
            Files.createDirectories(RUTA_ARCHIVO.getParent());
            Files.writeString(RUTA_ARCHIVO, convertirSucursalesAJson(), StandardCharsets.UTF_8);
        } catch (IOException excepcion) {
            throw new PersistenciaSucursalException("No se pudo guardar el archivo de sucursales.", excepcion);
        }
    }

    private String convertirSucursalesAJson() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int indice = 0; indice < sucursales.size(); indice++) {
            Sucursal sucursal = sucursales.get(indice);
            json.append("  {\n");
            agregarPropiedad(json, "numeroIdentificacion", sucursal.getNumeroIdentificacion(), true);
            agregarPropiedad(json, "nombre", sucursal.getNombre(), true);
            agregarPropiedad(json, "direccion", sucursal.getDireccion(), true);
            agregarPropiedad(json, "telefono", sucursal.getTelefono(), true);
            agregarPropiedad(json, "correo", sucursal.getCorreo(), true);
            agregarPropiedad(json, "nombreGerente", sucursal.getNombreGerente(), true);
            agregarPropiedad(json, "personaContacto", sucursal.getPersonaContacto(), false);
            json.append("  }");
            if (indice < sucursales.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");
        return json.toString();
    }

    private void agregarPropiedad(StringBuilder json, String nombre, String valor, boolean agregarComa) {
        json.append("    \"").append(nombre).append("\": \"").append(escapar(valor)).append("\"");
        if (agregarComa) {
            json.append(",");
        }
        json.append("\n");
    }

    private String escapar(String valor) {
        return valor == null ? "" : valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private List<Sucursal> convertirJsonASucursales(String contenido) {
        List<Sucursal> sucursalesLeidas = new ArrayList<Sucursal>();
        Pattern patronObjeto = Pattern.compile("\\{([^}]*)\\}", Pattern.DOTALL);
        Matcher coincidenciaObjeto = patronObjeto.matcher(contenido);
        while (coincidenciaObjeto.find()) {
            String objeto = coincidenciaObjeto.group(1);
            Sucursal sucursal = new Sucursal();
            sucursal.setNumeroIdentificacion(obtenerValor(objeto, "numeroIdentificacion"));
            sucursal.setNombre(obtenerValor(objeto, "nombre"));
            sucursal.setDireccion(obtenerValor(objeto, "direccion"));
            sucursal.setTelefono(obtenerValor(objeto, "telefono"));
            sucursal.setCorreo(obtenerValor(objeto, "correo"));
            sucursal.setNombreGerente(obtenerValor(objeto, "nombreGerente"));
            sucursal.setPersonaContacto(obtenerValor(objeto, "personaContacto"));
            sucursalesLeidas.add(sucursal);
        }
        return sucursalesLeidas;
    }

    private String obtenerValor(String objeto, String propiedad) {
        Pattern patronPropiedad = Pattern.compile("\\\"" + propiedad + "\\\"\\s*:\\s*\\\"(.*?)\\\"", Pattern.DOTALL);
        Matcher coincidencia = patronPropiedad.matcher(objeto);
        String valor = "";
        if (coincidencia.find()) {
            valor = coincidencia.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return valor;
    }
}
