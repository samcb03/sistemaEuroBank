package uv.lis.controlador;

import uv.lis.modelo.CatalogoTipoCuenta;
import uv.lis.modelo.Cliente;
import uv.lis.modelo.CuentaAhorros;
import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.CuentaCorriente;
import uv.lis.modelo.CuentaEmpresarial;
import uv.lis.modelo.Sucursal;
import uv.lis.modelo.SucursalDAO;
import uv.lis.modelo.DAO.implementacion.ClienteDAO;
import uv.lis.modelo.DAO.implementacion.CuentaDAO;
import uv.lis.modelo.excepcion.CuentaDuplicadaException;
import uv.lis.modelo.excepcion.CuentaNoEncontradaException;
import uv.lis.modelo.excepcion.SaldoInsuficienteException;
import uv.lis.vista.DialogoCuenta;
import uv.lis.vista.VistaCuenta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class CuentaControlador {

    private final CuentaDAO repositorioCuenta = new CuentaDAO();
    private final ClienteDAO repositorioCliente = new ClienteDAO();
    private final SucursalDAO repositorioSucursales = new SucursalDAO();
    private final DialogoCuenta dialogoCuenta = new DialogoCuenta();
    private VistaCuenta vista;
    private boolean propietarioDialogoEstablecido;

    public void iniciar(VistaCuenta vista) {
        this.vista = vista;
        vista.refrescarTabla(obtenerTodasLasCuentas());

        vista.obtenerBotonAgregar().setOnAction(e -> solicitarAbrirCuenta());
        vista.obtenerBotonCerrar().setOnAction(e -> solicitarCerrarCuenta());
        vista.obtenerBotonExportarCSV().setOnAction(e -> exportarCSV());
        vista.obtenerBotonExportarPDF().setOnAction(e -> exportarPDF());
    }

    public void abrirCuenta(CuentaBancaria cuenta)
            throws CuentaDuplicadaException {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank()) {
            throw new IllegalArgumentException("El número de cuenta es obligatorio.");
        }
        repositorioCuenta.agregar(cuenta);
    }

    public void cerrarCuenta(String numeroCuenta)
            throws CuentaNoEncontradaException, SaldoInsuficienteException {
        CuentaBancaria cuenta = buscarCuentaOExcepcion(numeroCuenta);
        if (cuenta.getSaldo() != 0) {
            throw new SaldoInsuficienteException(
                "La cuenta " + numeroCuenta + " aún tiene saldo $"
                + cuenta.getSaldo() + ". Debe ser $0 para cerrarla.");
        }
        repositorioCuenta.eliminar(numeroCuenta);
    }

    public void modificarLimiteCredito(String numeroCuenta, double nuevoLimite)
            throws CuentaNoEncontradaException {
        CuentaBancaria cuenta = buscarCuentaOExcepcion(numeroCuenta);
        if (!(cuenta instanceof CuentaEmpresarial)) {
            throw new IllegalArgumentException(
                "Solo las cuentas empresariales permiten modificar el límite de crédito.");
        }
        ((CuentaEmpresarial) cuenta).setLimiteCredito(nuevoLimite);
        repositorioCuenta.actualizar(cuenta);
    }

    public Optional<CuentaBancaria> buscarCuenta(String numeroCuenta) {
        return repositorioCuenta.buscarPorNumero(numeroCuenta);
    }

    public boolean existeCuenta(String numeroCuenta) {
        return repositorioCuenta.existe(numeroCuenta);
    }

    public List<CuentaBancaria> obtenerTodasLasCuentas() {
        return repositorioCuenta.obtenerTodas();
    }

    private void solicitarAbrirCuenta() {
        cargarCatalogosEnDialogo();
        asegurarPropietarioDialogo();
        dialogoCuenta.prepararParaAgregar();
        procesarCaptura();
    }

    private void cargarCatalogosEnDialogo() {
        List<String> rfcClientes = new ArrayList<String>();
        for (Cliente cliente : repositorioCliente.obtenerTodos()) {
            rfcClientes.add(cliente.getRfcCurp());
        }
        List<String> idsSucursal = new ArrayList<String>();
        for (Sucursal sucursal : repositorioSucursales.obtenerTodas()) {
            idsSucursal.add(sucursal.getNumeroIdentificacion());
        }
        dialogoCuenta.cargarClientes(rfcClientes);
        dialogoCuenta.cargarSucursales(idsSucursal);
    }

    private void procesarCaptura() {
        boolean capturaPendiente = true;
        while (capturaPendiente && dialogoCuenta.mostrarYConfirmar()) {
            capturaPendiente = !intentarAbrir();
        }
    }

    private boolean intentarAbrir() {
        boolean exito;
        try {
            CuentaBancaria cuenta = construirCuentaDesdeDialogo();
            abrirCuenta(cuenta);
            vista.refrescarTabla(obtenerTodasLasCuentas());
            vista.mostrarMensajeInformacion("Cuenta abierta correctamente.");
            exito = true;
        } catch (CuentaDuplicadaException ex) {
            vista.mostrarMensajeError(ex.getMessage());
            exito = false;
        } catch (NumberFormatException ex) {
            vista.mostrarMensajeError("Saldo, tasa o límite deben ser numéricos.");
            exito = false;
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError("Datos inválidos: " + ex.getMessage());
            exito = false;
        }
        return exito;
    }

    private CuentaBancaria construirCuentaDesdeDialogo() {
        String numero = dialogoCuenta.obtenerNumeroCuenta();
        String tipo = dialogoCuenta.obtenerTipoSeleccionado();
        double saldo = parsearODefecto(dialogoCuenta.obtenerSaldoInicialTexto());
        Cliente cliente = resolverCliente(dialogoCuenta.obtenerRfcClienteSeleccionado());
        String idSucursal = dialogoCuenta.obtenerSucursalSeleccionada();
        CuentaBancaria cuenta = crearCuentaPorTipo(tipo, numero, saldo, cliente, idSucursal);
        return cuenta;
    }

    private CuentaBancaria crearCuentaPorTipo(String tipo, String numero, double saldo,
            Cliente cliente, String idSucursal) {
        CuentaBancaria cuenta;
        if (CatalogoTipoCuenta.AHORROS.equals(tipo)) {
            double tasa = parsearODefecto(dialogoCuenta.obtenerTasaInteresTexto());
            cuenta = new CuentaAhorros(numero, saldo, cliente, idSucursal, tasa);
        } else if (CatalogoTipoCuenta.CORRIENTE.equals(tipo)) {
            double limite = parsearODefecto(dialogoCuenta.obtenerLimiteCreditoTexto());
            cuenta = new CuentaCorriente(numero, saldo, cliente, idSucursal, limite);
        } else if (CatalogoTipoCuenta.EMPRESARIAL.equals(tipo)) {
            cuenta = construirCuentaEmpresarial(numero, saldo, cliente, idSucursal);
        } else {
            throw new IllegalArgumentException("Seleccione un tipo de cuenta.");
        }
        return cuenta;
    }

    private CuentaBancaria construirCuentaEmpresarial(String numero, double saldo,
            Cliente cliente, String idSucursal) {
        CuentaEmpresarial cuenta = new CuentaEmpresarial(numero, saldo, cliente);
        cuenta.setIdSucursal(idSucursal);
        cuenta.setLimiteCredito(parsearODefecto(dialogoCuenta.obtenerLimiteCreditoTexto()));
        cuenta.setRazonSocial(dialogoCuenta.obtenerRazonSocial());
        return cuenta;
    }

    private Cliente resolverCliente(String rfcCurp) {
        Optional<Cliente> clienteEncontrado = repositorioCliente.buscarPorRfc(rfcCurp);
        if (!clienteEncontrado.isPresent()) {
            throw new IllegalArgumentException("Seleccione un cliente válido.");
        }
        return clienteEncontrado.get();
    }

    private double parsearODefecto(String texto) {
        double valor = 0.0;
        if (!texto.isEmpty()) {
            valor = Double.parseDouble(texto);
        }
        return valor;
    }

    private void solicitarCerrarCuenta() {
        CuentaBancaria seleccionada = vista.obtenerCuentaSeleccionada();
        if (seleccionada == null) {
            vista.mostrarMensajeError("Seleccione una cuenta para cerrar.");
        } else {
            confirmarYCerrar(seleccionada);
        }
    }

    private void confirmarYCerrar(CuentaBancaria seleccionada) {
        if (vista.confirmarCierre(seleccionada.getNumeroCuenta())) {
            try {
                cerrarCuenta(seleccionada.getNumeroCuenta());
                vista.refrescarTabla(obtenerTodasLasCuentas());
                vista.mostrarMensajeInformacion("Cuenta cerrada correctamente.");
            } catch (CuentaNoEncontradaException | SaldoInsuficienteException ex) {
                vista.mostrarMensajeError(ex.getMessage());
            }
        }
    }

    private void exportarCSV() {
        File destino = elegirArchivo("Archivo CSV", "*.csv");
        if (destino != null) {
            escribirCSV(destino);
        }
    }

    private void escribirCSV(File destino) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(destino))) {
            escritor.println("No. Cuenta,Tipo,Saldo,Limite Credito,Cliente");
            for (CuentaBancaria cuenta : obtenerTodasLasCuentas()) {
                escritor.println(String.join(",",
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta(),
                    String.format("%.2f", cuenta.getSaldo()),
                    String.format("%.2f", cuenta.getLimiteCredito()),
                    cuenta.getRfcCurpCliente()));
            }
            vista.mostrarMensajeInformacion("Cuentas exportadas a CSV: " + destino.getName());
        } catch (IOException ex) {
            vista.mostrarMensajeError("No se pudo escribir el archivo CSV: " + ex.getMessage());
        }
    }

    private void exportarPDF() {
        File destino = elegirArchivo("Archivo PDF", "*.pdf");
        if (destino != null) {
            escribirPDF(destino);
        }
    }

    private void escribirPDF(File destino) {
        try (PDDocument documento = new PDDocument()) {
            escribirContenidoPDF(documento, obtenerTodasLasCuentas());
            documento.save(destino);
            vista.mostrarMensajeInformacion("Cuentas exportadas a PDF: " + destino.getName());
        } catch (IOException ex) {
            vista.mostrarMensajeError("No se pudo escribir el archivo PDF: " + ex.getMessage());
        }
    }

    private void escribirContenidoPDF(PDDocument documento, List<CuentaBancaria> cuentas)
            throws IOException {
        PDPage pagina = new PDPage(PDRectangle.A4);
        documento.addPage(pagina);
        PDPageContentStream contenido = new PDPageContentStream(documento, pagina);
        escribirLinea(contenido, "EuroBank - Reporte de Cuentas", 50, 780, true);
        float coordenadaY = 750;
        for (CuentaBancaria cuenta : cuentas) {
            if (coordenadaY < 60) {
                contenido.close();
                pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);
                contenido = new PDPageContentStream(documento, pagina);
                coordenadaY = 780;
            }
            escribirLinea(contenido, formatoLineaCuenta(cuenta), 50, coordenadaY, false);
            coordenadaY -= 18;
        }
        contenido.close();
    }

    private void escribirLinea(PDPageContentStream contenido, String texto,
            float coordenadaX, float coordenadaY, boolean negrita) throws IOException {
        if (negrita) {
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 14);
        } else {
            contenido.setFont(PDType1Font.HELVETICA, 10);
        }
        contenido.beginText();
        contenido.newLineAtOffset(coordenadaX, coordenadaY);
        contenido.showText(texto);
        contenido.endText();
    }

    private String formatoLineaCuenta(CuentaBancaria cuenta) {
        String linea = cuenta.getNumeroCuenta() + "   |   " + cuenta.getTipoCuenta()
            + "   |   Saldo: $" + String.format("%.2f", cuenta.getSaldo())
            + "   |   Limite: $" + String.format("%.2f", cuenta.getLimiteCredito())
            + "   |   Cliente: " + cuenta.getRfcCurpCliente();
        return linea;
    }

    private File elegirArchivo(String descripcion, String patron) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Exportar cuentas");
        selector.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(descripcion, patron));
        File archivo = selector.showSaveDialog(vista.obtenerEscena().getWindow());
        return archivo;
    }

    private CuentaBancaria buscarCuentaOExcepcion(String numeroCuenta)
            throws CuentaNoEncontradaException {
        Optional<CuentaBancaria> cuentaEncontrada = repositorioCuenta.buscarPorNumero(numeroCuenta);
        if (!cuentaEncontrada.isPresent()) {
            throw new CuentaNoEncontradaException(
                "No se encontró ninguna cuenta con el número: " + numeroCuenta);
        }
        return cuentaEncontrada.get();
    }

    private void asegurarPropietarioDialogo() {
        if (!propietarioDialogoEstablecido) {
            dialogoCuenta.establecerVentanaPropietaria(vista.obtenerEscena().getWindow());
            propietarioDialogoEstablecido = true;
        }
    }
}