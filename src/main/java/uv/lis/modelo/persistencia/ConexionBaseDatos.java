package uv.lis.modelo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uv.lis.modelo.excepcion.AccesoDatosException;

public final class ConexionBaseDatos {

    private ConexionBaseDatos() {
    }

    public static Connection abrir() {
        Connection conexion;
        try {
            conexion = DriverManager.getConnection(
                    ConfiguracionBaseDatos.URL,
                    ConfiguracionBaseDatos.USUARIO,
                    ConfiguracionBaseDatos.CONTRASENIA);
        } catch (SQLException excepcion) {
            throw new AccesoDatosException(
                    "No se pudo establecer la conexion con la base de datos.", excepcion);
        }
        return conexion;
    }
}