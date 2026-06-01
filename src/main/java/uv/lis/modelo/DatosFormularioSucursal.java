package uv.lis.modelo;

public class DatosFormularioSucursal {

    private final String idSucursal;
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final String correo;
    private final String nombreGerente;
    private final String personaContacto;

    private DatosFormularioSucursal(Constructor constructor) {
        this.idSucursal = constructor.idSucursal;
        this.nombre = constructor.nombre;
        this.direccion = constructor.direccion;
        this.telefono = constructor.telefono;
        this.correo = constructor.correo;
        this.nombreGerente = constructor.nombreGerente;
        this.personaContacto = constructor.personaContacto;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombreGerente() {
        return nombreGerente;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public static class Constructor {

        private String idSucursal;
        private String nombre;
        private String direccion;
        private String telefono;
        private String correo;
        private String nombreGerente;
        private String personaContacto;

        public Constructor conIdSucursal(String idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public Constructor conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Constructor conDireccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Constructor conTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Constructor conCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public Constructor conNombreGerente(String nombreGerente) {
            this.nombreGerente = nombreGerente;
            return this;
        }

        public Constructor conPersonaContacto(String personaContacto) {
            this.personaContacto = personaContacto;
            return this;
        }

        public DatosFormularioSucursal construir() {
            DatosFormularioSucursal datos = new DatosFormularioSucursal(this);
            return datos;
        }
    }
}