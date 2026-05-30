package uv.lis.modelo;

import java.time.LocalDate;

public class DatosFormularioEmpleado {

    private final String idEmpleado;
    private final String nombreCompleto;
    private final String direccion;
    private final LocalDate fechaNacimiento;
    private final Genero genero;
    private final String salarioTexto;
    private final String nombreUsuario;
    private final String contrasenia;
    private final Rol rol;
    private final String horaInicioTurnoTexto;
    private final String horaFinTurnoTexto;
    private final String numeroVentanillaTexto;
    private final String numeroClientesTexto;
    private final EspecializacionEjecutivo especializacion;
    private final NivelAccesoGerente nivelAcceso;
    private final String aniosExperienciaTexto;

    private DatosFormularioEmpleado(Constructor constructor) {
        this.idEmpleado = constructor.idEmpleado;
        this.nombreCompleto = constructor.nombreCompleto;
        this.direccion = constructor.direccion;
        this.fechaNacimiento = constructor.fechaNacimiento;
        this.genero = constructor.genero;
        this.salarioTexto = constructor.salarioTexto;
        this.nombreUsuario = constructor.nombreUsuario;
        this.contrasenia = constructor.contrasenia;
        this.rol = constructor.rol;
        this.horaInicioTurnoTexto = constructor.horaInicioTurnoTexto;
        this.horaFinTurnoTexto = constructor.horaFinTurnoTexto;
        this.numeroVentanillaTexto = constructor.numeroVentanillaTexto;
        this.numeroClientesTexto = constructor.numeroClientesTexto;
        this.especializacion = constructor.especializacion;
        this.nivelAcceso = constructor.nivelAcceso;
        this.aniosExperienciaTexto = constructor.aniosExperienciaTexto;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public String getSalarioTexto() {
        return salarioTexto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public String getHoraInicioTurnoTexto() {
        return horaInicioTurnoTexto;
    }

    public String getHoraFinTurnoTexto() {
        return horaFinTurnoTexto;
    }

    public String getNumeroVentanillaTexto() {
        return numeroVentanillaTexto;
    }

    public String getNumeroClientesTexto() {
        return numeroClientesTexto;
    }

    public EspecializacionEjecutivo getEspecializacion() {
        return especializacion;
    }

    public NivelAccesoGerente getNivelAcceso() {
        return nivelAcceso;
    }

    public String getAniosExperienciaTexto() {
        return aniosExperienciaTexto;
    }
    
    public static class Constructor {

        private String idEmpleado;
        private String nombreCompleto;
        private String direccion;
        private LocalDate fechaNacimiento;
        private Genero genero;
        private String salarioTexto;
        private String nombreUsuario;
        private String contrasenia;
        private Rol rol;
        private String horaInicioTurnoTexto;
        private String horaFinTurnoTexto;
        private String numeroVentanillaTexto;
        private String numeroClientesTexto;
        private EspecializacionEjecutivo especializacion;
        private NivelAccesoGerente nivelAcceso;
        private String aniosExperienciaTexto;

        public Constructor conIdEmpleado(String idEmpleado) {
            this.idEmpleado = idEmpleado;
            return this;
        }

        public Constructor conNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
            return this;
        }

        public Constructor conDireccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Constructor conFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Constructor conGenero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Constructor conSalarioTexto(String salarioTexto) {
            this.salarioTexto = salarioTexto;
            return this;
        }

        public Constructor conNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public Constructor conContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
            return this;
        }

        public Constructor conRol(Rol rol) {
            this.rol = rol;
            return this;
        }

        public Constructor conHoraInicioTurnoTexto(String horaInicioTurnoTexto) {
            this.horaInicioTurnoTexto = horaInicioTurnoTexto;
            return this;
        }

        public Constructor conHoraFinTurnoTexto(String horaFinTurnoTexto) {
            this.horaFinTurnoTexto = horaFinTurnoTexto;
            return this;
        }

        public Constructor conNumeroVentanillaTexto(String numeroVentanillaTexto) {
            this.numeroVentanillaTexto = numeroVentanillaTexto;
            return this;
        }

        public Constructor conNumeroClientesTexto(String numeroClientesTexto) {
            this.numeroClientesTexto = numeroClientesTexto;
            return this;
        }

        public Constructor conEspecializacion(EspecializacionEjecutivo especializacion) {
            this.especializacion = especializacion;
            return this;
        }

        public Constructor conNivelAcceso(NivelAccesoGerente nivelAcceso) {
            this.nivelAcceso = nivelAcceso;
            return this;
        }

        public Constructor conAniosExperienciaTexto(String aniosExperienciaTexto) {
            this.aniosExperienciaTexto = aniosExperienciaTexto;
            return this;
        }

        public DatosFormularioEmpleado construir() {
            DatosFormularioEmpleado datos = new DatosFormularioEmpleado(this);
            return datos;
        }
    }
}