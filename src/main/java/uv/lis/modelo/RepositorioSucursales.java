package uv.lis.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import uv.lis.modelo.excepcion.SucursalDuplicadaException;
import uv.lis.modelo.excepcion.SucursalNoEncontradaException;

public class RepositorioSucursales {

    private static final int INDICE_NO_ENCONTRADO = -1;

    private final List<Sucursal> sucursales;

    public RepositorioSucursales() {
        this.sucursales = new ArrayList<Sucursal>();
        cargarSucursalesIniciales();
    }

    private void cargarSucursalesIniciales() {
        sucursales.add(new Sucursal("S001", "Sucursal Centro", "Av. Central 100",
                "55-1000-2000", "centro@eurobank.com", "Maria Lopez", "Carlos Ruiz"));
        sucursales.add(new Sucursal("S002", "Sucursal Norte", "Calle Norte 250",
                "55-3000-4000", "norte@eurobank.com", "Pedro Diaz", "Laura Mena"));
    }

    public Optional<Sucursal> buscarPorId(String idSucursal) {
        Optional<Sucursal> sucursalEncontrada = Optional.empty();
        for (Sucursal sucursal : sucursales) {
            if (sucursal.getIdSucursal().equals(idSucursal)) {
                sucursalEncontrada = Optional.of(sucursal);
            }
        }
        return sucursalEncontrada;
    }

    public List<Sucursal> obtenerTodas() {
        List<Sucursal> copiaSucursales = new ArrayList<Sucursal>(sucursales);
        return copiaSucursales;
    }

    public void agregar(Sucursal sucursal) throws SucursalDuplicadaException {
        if (existeId(sucursal.getIdSucursal())) {
            throw new SucursalDuplicadaException(
                    "Ya existe una sucursal con el ID " + sucursal.getIdSucursal() + ".");
        }
        sucursales.add(sucursal);
    }

    public void actualizar(Sucursal sucursal) throws SucursalNoEncontradaException {
        int indice = obtenerIndicePorId(sucursal.getIdSucursal());
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new SucursalNoEncontradaException(
                    "No se encontro la sucursal con ID " + sucursal.getIdSucursal() + ".");
        }
        sucursales.set(indice, sucursal);
    }

    public void eliminar(String idSucursal) throws SucursalNoEncontradaException {
        int indice = obtenerIndicePorId(idSucursal);
        if (indice == INDICE_NO_ENCONTRADO) {
            throw new SucursalNoEncontradaException(
                    "No se encontro la sucursal con ID " + idSucursal + ".");
        }
        sucursales.remove(indice);
    }

    private boolean existeId(String idSucursal) {
        boolean existe = obtenerIndicePorId(idSucursal) != INDICE_NO_ENCONTRADO;
        return existe;
    }

    private int obtenerIndicePorId(String idSucursal) {
        int indiceEncontrado = INDICE_NO_ENCONTRADO;
        for (int indice = 0; indice < sucursales.size(); indice++) {
            if (sucursales.get(indice).getIdSucursal().equals(idSucursal)) {
                indiceEncontrado = indice;
            }
        }
        return indiceEncontrado;
    }
}