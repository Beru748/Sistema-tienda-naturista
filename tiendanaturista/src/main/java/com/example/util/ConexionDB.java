package com.example.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // 1. Definir las credenciales y la URL de la base de datos
    // Cambia "localhost" si tu BD está en otro servidor.
    // "1521" es el puerto por defecto de Oracle.
    // "xe" o "ORCL" es el SID o nombre del servicio (cámbialo según tu configuración).
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USUARIO = "tnaturista";
    private static final String CONTRASENA = "camilo123";

    // Variable para almacenar la conexión
    private static Connection conexion = null;

    // Constructor privado para evitar que se instancie la clase
    private ConexionDB() {
    }

    // Método estático para obtener la conexión
    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                // 2. Registrar el Driver (Opcional en versiones recientes de JDBC, pero buena práctica)
                Class.forName("oracle.jdbc.OracleDriver");

                // 3. Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("¡Conexion exitosa a la base de datos Tienda naturista!");

            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se encontró el driver de Oracle. Verifica que el ojdbc.jar esté en el proyecto.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Error de conexión a la base de datos. Verifica tus credenciales y URL.");
                e.printStackTrace();
            }
        }
        return conexion;
    }

    // Método para cerrar la conexión cuando ya no se necesite
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}