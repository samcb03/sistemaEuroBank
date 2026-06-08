package uv.lis.modelo;

public final class PermisosEmpleado {

    private final boolean administrarEmpleados;
    private final boolean gestionarSucursales;
    private final boolean gestionarCuentas;
    private final boolean registrarTransacciones;
    private final boolean gestionarClientes;

    PermisosEmpleado(boolean administrarEmpleados,
                     boolean gestionarSucursales,
                     boolean gestionarCuentas,
                     boolean registrarTransacciones,
                     boolean gestionarClientes) {
        this.administrarEmpleados = administrarEmpleados;
        this.gestionarSucursales = gestionarSucursales;
        this.gestionarCuentas = gestionarCuentas;
        this.registrarTransacciones = registrarTransacciones;
        this.gestionarClientes = gestionarClientes;
    }

    public static ConstructorPermisosEmpleado constructor() {
        ConstructorPermisosEmpleado constructor = new ConstructorPermisosEmpleado();
        return constructor;
    }

    public boolean puedeAdministrarEmpleados() {
        return administrarEmpleados;
    }

    public boolean puedeGestionarSucursales() {
        return gestionarSucursales;
    }

    public boolean puedeGestionarCuentas() {
        return gestionarCuentas;
    }

    public boolean puedeRegistrarTransacciones() {
        return registrarTransacciones;
    }

    public boolean puedeGestionarClientes() {
        return gestionarClientes;
    }
}