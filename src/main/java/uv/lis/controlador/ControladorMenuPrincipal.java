package uv.lis.controlador;

import java.util.ArrayList;
import javafx.stage.Stage;
import uv.lis.modelo.CuentaBancaria;
import uv.lis.modelo.RepositorioEmpleados;
import uv.lis.modelo.RepositorioSucursales;
import uv.lis.modelo.Rol;
import uv.lis.vista.VistaCliente;
import uv.lis.vista.VistaCuenta;
import uv.lis.vista.VistaEmpleados;
import uv.lis.vista.VistaLogin;
import uv.lis.vista.VistaMenuPrincipal;
import uv.lis.vista.VistaSucursales;
import uv.lis.vista.VistaTransaccion;

public class ControladorMenuPrincipal {

    private static final String TITULO_EMPLEADOS    = "EuroBank - Administracion de Empleados";
    private static final String TITULO_SUCURSALES   = "EuroBank - Administracion de Sucursales";
    private static final String TITULO_CLIENTES     = "EuroBank - Clientes";
    private static final String TITULO_CUENTAS      = "EuroBank - Cuentas Bancarias";
    private static final String TITULO_TRANSACCIONES = "EuroBank - Transacciones";
    private static final String TITULO_LOGIN        = "EuroBank - Acceso al Sistema";

    private final VistaMenuPrincipal   vistaMenuPrincipal;
    private final RepositorioEmpleados repositorioEmpleados;
    private final RepositorioSucursales repositorioSucursales;
    private final Stage                escenarioPrincipal;
    private final VistaLogin           vistaLogin;

    public ControladorMenuPrincipal(VistaMenuPrincipal vistaMenuPrincipal,
                                    RepositorioEmpleados repositorioEmpleados,
                                    RepositorioSucursales repositorioSucursales,
                                    Stage escenarioPrincipal,
                                    VistaLogin vistaLogin) {
        this.vistaMenuPrincipal    = vistaMenuPrincipal;
        this.repositorioEmpleados  = repositorioEmpleados;
        this.repositorioSucursales = repositorioSucursales;
        this.escenarioPrincipal    = escenarioPrincipal;
        this.vistaLogin            = vistaLogin;
    }

    public void iniciar(Rol rolAutenticado) {
        configurarPermisos(rolAutenticado);
        registrarManejadores();
    }

    private void configurarPermisos(Rol rolAutenticado) {
        boolean puedeAdministrar =
            rolAutenticado == Rol.ADMINISTRADOR || rolAutenticado == Rol.GERENTE;
        vistaMenuPrincipal.obtenerBotonEmpleados().setDisable(!puedeAdministrar);
    }

    private void registrarManejadores() {
        vistaMenuPrincipal.obtenerBotonEmpleados().setOnAction(evento -> abrirEmpleados());
        vistaMenuPrincipal.obtenerBotonSucursales().setOnAction(evento -> abrirSucursales());
        vistaMenuPrincipal.obtenerBotonClientes().setOnAction(evento -> abrirClientes());
        vistaMenuPrincipal.obtenerBotonCuentas().setOnAction(evento -> abrirCuentas());
        vistaMenuPrincipal.obtenerBotonTransacciones().setOnAction(evento -> abrirTransacciones());
        vistaMenuPrincipal.obtenerBotonCerrarSesion().setOnAction(evento -> cerrarSesion());
    }

    private void abrirEmpleados() {
        VistaEmpleados vistaEmpleados = new VistaEmpleados();
        ControladorEmpleados controladorEmpleados =
            new ControladorEmpleados(vistaEmpleados, repositorioEmpleados);
        controladorEmpleados.iniciar();
        mostrarVentanaSecundaria(TITULO_EMPLEADOS, vistaEmpleados.obtenerEscena());
    }

    private void abrirSucursales() {
        VistaSucursales vistaSucursales = new VistaSucursales();
        ControladorSucursales controladorSucursales =
            new ControladorSucursales(vistaSucursales, repositorioSucursales);
        controladorSucursales.iniciar();
        mostrarVentanaSecundaria(TITULO_SUCURSALES, vistaSucursales.obtenerEscena());
    }

    // CORRECCIÓN: se crea el ControladorCliente y se llama iniciar()
    // para que los botones Agregar / Editar / Eliminar funcionen.
    private void abrirClientes() {
        VistaCliente vistaCliente = new VistaCliente();
        ControladorCliente controladorCliente = new ControladorCliente();
        controladorCliente.iniciar(vistaCliente);
        mostrarVentanaSecundaria(TITULO_CLIENTES, vistaCliente.obtenerEscena());
    }

    // CORRECCIÓN: se crea el CuentaControlador y se llama iniciar()
    // para que los botones Abrir / Cerrar Cuenta funcionen.
    private void abrirCuentas() {
        VistaCuenta vistaCuenta = new VistaCuenta();
        CuentaControlador controladorCuenta = new CuentaControlador();
        controladorCuenta.iniciar(vistaCuenta);
        mostrarVentanaSecundaria(TITULO_CUENTAS, vistaCuenta.obtenerEscena());
    }

    private void abrirTransacciones() {
        VistaTransaccion vistaTransaccion = new VistaTransaccion();
        TransaccionControlador controladorTransaccion =
            new TransaccionControlador(new ArrayList<CuentaBancaria>());
        vistaTransaccion.obtenerBotonLimpiar()
            .setOnAction(evento -> vistaTransaccion.limpiarFormulario());
        vistaTransaccion.obtenerBotonEjecutar()
            .setOnAction(evento -> vistaTransaccion.mostrarMensajeInformacion(
                "Módulo de transacciones abierto. "
                + "La lógica bancaria queda lista para conectarse con cuentas reales."));
        controladorTransaccion.obtenerTiposDisponibles();
        mostrarVentanaSecundaria(TITULO_TRANSACCIONES, vistaTransaccion.obtenerEscena());
    }

    private void mostrarVentanaSecundaria(String titulo, javafx.scene.Scene escena) {
        Stage escenario = new Stage();
        escenario.setTitle(titulo);
        escenario.setScene(escena);
        escenario.show();
    }

    private void cerrarSesion() {
        escenarioPrincipal.setTitle(TITULO_LOGIN);
        escenarioPrincipal.setScene(vistaLogin.obtenerEscena());
        vistaLogin.limpiarContrasenia();
        vistaLogin.limpiarMensaje();
    }
}