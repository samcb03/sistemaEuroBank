package uv.lis.controlador;

import javafx.scene.Scene;
import javafx.stage.Stage;
import uv.lis.modelo.Empleado;
import uv.lis.modelo.RepositorioEmpleados;
import uv.lis.modelo.RepositorioSucursales;
import uv.lis.vista.VistaCliente;
import uv.lis.vista.VistaCuenta;
import uv.lis.vista.VistaEmpleados;
import uv.lis.vista.VistaMenuPrincipal;
import uv.lis.vista.VistaSucursales;
import uv.lis.vista.VistaTransaccion;

public class ControladorMenuPrincipal {

    private static final String TITULO_EMPLEADOS = "EuroBank - Empleados";
    private static final String TITULO_SUCURSALES = "EuroBank - Sucursales";
    private static final String TITULO_CLIENTES = "EuroBank - Clientes";
    private static final String TITULO_CUENTAS = "EuroBank - Cuentas";
    private static final String TITULO_TRANSACCIONES = "EuroBank - Transacciones";

    private final VistaMenuPrincipal vistaMenu;
    private final Empleado empleadoAutenticado;
    private final RepositorioEmpleados repositorioEmpleados;
    private final RepositorioSucursales repositorioSucursales;
    private final Stage escenarioMenu;
    private final Stage escenarioLogin;

    public ControladorMenuPrincipal(VistaMenuPrincipal vistaMenu,
                                    Empleado empleadoAutenticado,
                                    RepositorioEmpleados repositorioEmpleados,
                                    Stage escenarioMenu,
                                    Stage escenarioLogin) {
        this.vistaMenu = vistaMenu;
        this.empleadoAutenticado = empleadoAutenticado;
        this.repositorioEmpleados = repositorioEmpleados;
        this.repositorioSucursales = new RepositorioSucursales();
        this.escenarioMenu = escenarioMenu;
        this.escenarioLogin = escenarioLogin;
    }

    public void iniciar() {
        vistaMenu.mostrarUsuario(
                empleadoAutenticado.getNombreCompleto(),
                empleadoAutenticado.obtenerDescripcionPuesto());
        registrarManejadores();
    }

    private void registrarManejadores() {
        vistaMenu.obtenerBotonEmpleados()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.EMPLEADOS));
        vistaMenu.obtenerBotonSucursales()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.SUCURSALES));
        vistaMenu.obtenerBotonClientes()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.CLIENTES));
        vistaMenu.obtenerBotonCuentas()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.CUENTAS));
        vistaMenu.obtenerBotonTransacciones()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.TRANSACCIONES));
        vistaMenu.obtenerBotonCerrarSesion()
                .setOnAction(new ManejadorOpcionMenu(this, OpcionMenu.CERRAR_SESION));
    }

    public void atenderOpcion(OpcionMenu opcion) {
        switch (opcion) {
            case EMPLEADOS:
                abrirModuloEmpleados();
                break;
            case SUCURSALES:
                abrirModuloSucursales();
                break;
            case CLIENTES:
                abrirModuloClientes();
                break;
            case CUENTAS:
                abrirModuloCuentas();
                break;
            case TRANSACCIONES:
                abrirModuloTransacciones();
                break;
            case CERRAR_SESION:
                cerrarSesion();
                break;
            default:
                break;
        }
    }

    private void abrirModuloEmpleados() {
        VistaEmpleados vistaEmpleados = new VistaEmpleados();
        ControladorEmpleados controladorEmpleados =
                new ControladorEmpleados(vistaEmpleados, repositorioEmpleados);
        controladorEmpleados.iniciar();
        abrirVentanaModulo(TITULO_EMPLEADOS, vistaEmpleados.obtenerEscena());
    }

    private void abrirModuloSucursales() {
        VistaSucursales vistaSucursales = new VistaSucursales();
        ControladorSucursales controladorSucursales =
                new ControladorSucursales(vistaSucursales, repositorioSucursales);
        controladorSucursales.iniciar();
        abrirVentanaModulo(TITULO_SUCURSALES, vistaSucursales.obtenerEscena());
    }

    private void abrirModuloClientes() {
        VistaCliente vistaCliente = new VistaCliente();
        abrirVentanaModulo(TITULO_CLIENTES, vistaCliente.obtenerEscena());
    }

    private void abrirModuloCuentas() {
        VistaCuenta vistaCuenta = new VistaCuenta();
        abrirVentanaModulo(TITULO_CUENTAS, vistaCuenta.obtenerEscena());
    }

    private void abrirModuloTransacciones() {
        VistaTransaccion vistaTransaccion = new VistaTransaccion();
        abrirVentanaModulo(TITULO_TRANSACCIONES, vistaTransaccion.obtenerEscena());
    }

    private void abrirVentanaModulo(String titulo, Scene escenaModulo) {
        Stage escenarioModulo = new Stage();
        escenarioModulo.setTitle(titulo);
        escenarioModulo.setScene(escenaModulo);
        escenarioModulo.show();
    }

    private void cerrarSesion() {
        escenarioMenu.close();
        escenarioLogin.show();
    }

    public enum OpcionMenu {
        EMPLEADOS,
        SUCURSALES,
        CLIENTES,
        CUENTAS,
        TRANSACCIONES,
        CERRAR_SESION
    }
}