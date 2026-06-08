package uv.lis.modelo;

public final class ConstructorPermisosEmpleado {

    private boolean administrarEmpleados;
    private boolean gestionarSucursales;
    private boolean gestionarCuentas;
    private boolean registrarTransacciones;
    private boolean gestionarClientes;

    public ConstructorPermisosEmpleado permitirAdministrarEmpleados() {
        this.administrarEmpleados = true;
        return this;
    }

    public ConstructorPermisosEmpleado permitirGestionarSucursales() {
        this.gestionarSucursales = true;
        return this;
    }

    public ConstructorPermisosEmpleado permitirGestionarCuentas() {
        this.gestionarCuentas = true;
        return this;
    }

    public ConstructorPermisosEmpleado permitirRegistrarTransacciones() {
        this.registrarTransacciones = true;
        return this;
    }

    public ConstructorPermisosEmpleado permitirGestionarClientes() {
        this.gestionarClientes = true;
        return this;
    }

    public PermisosEmpleado construir() {
        PermisosEmpleado permisos = new PermisosEmpleado(
                administrarEmpleados,
                gestionarSucursales,
                gestionarCuentas,
                registrarTransacciones,
                gestionarClientes);
        return permisos;
    }
}