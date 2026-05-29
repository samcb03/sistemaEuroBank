package uv.lis.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioEmpleados {

    private final List<Empleado> empleados;

    public RepositorioEmpleados() {
        this.empleados = new ArrayList<Empleado>();
        cargarEmpleadosIniciales();
    }

    private void cargarEmpleadosIniciales() {
        empleados.add(new Administrador("E001", "Administrador General", "Oficina Central",
                LocalDate.of(1980, 1, 15), Genero.OTRO, 90000.0, "root", "admin123"));
        empleados.add(new Gerente("E002", "Maria Lopez", "Av. Principal 100",
                LocalDate.of(1985, 5, 20), Genero.FEMENINO, 70000.0, "mlopez", "gerente123",
                NivelAccesoGerente.NACIONAL, 12));
        empleados.add(new Cajero("E003", "Juan Perez", "Calle 2 #45",
                LocalDate.of(1995, 3, 10), Genero.MASCULINO, 30000.0, "jperez", "cajero123",
                LocalTime.of(9, 0), LocalTime.of(17, 0), 4));
        empleados.add(new EjecutivoCuenta("E004", "Ana Gomez", "Calle 3 #78",
                LocalDate.of(1990, 7, 25), Genero.FEMENINO, 45000.0, "agomez", "ejecutivo123",
                15, EspecializacionEjecutivo.PYMES));
    }

    public Optional<Empleado> buscarPorNombreUsuario(String nombreUsuario) {
        Optional<Empleado> empleadoEncontrado = Optional.empty();
        for (Empleado empleado : empleados) {
            if (empleado.getNombreUsuario().equals(nombreUsuario)) {
                empleadoEncontrado = Optional.of(empleado);
            }
        }
        return empleadoEncontrado;
    }
}