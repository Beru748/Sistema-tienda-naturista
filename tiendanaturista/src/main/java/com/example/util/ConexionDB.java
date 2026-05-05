package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConexionDB — Conexion a Oracle usando archivo de configuracion externo.
 *
 * Cada desarrollador tiene su propio archivo db.properties en
 * src/main/resources con su IP, usuario y contrasena.
 * El codigo Java no cambia — solo cambia el archivo de configuracion.
 *
 * Desarrolladores:
 *  - Camilo → localhost
 *  - Erik   → 192.168.1.5 (IP de Camilo)
 *  - Felix  → 192.168.1.5 (IP de Camilo)
 * Todos usan usuario tnaturista / camilo123
 */
public class ConexionDB {

    //Son los datos leidos del archivo db.properties
    //private solo usa esta clase, static porque pertenecen a la clas y no a una instancia
    private static String url;
    private static String usuario;
    private static String contrasena;

    
    private static Connection conexion = null;

     //bloque que se ejecuta una sola vez cuando la clase se carga en memoria
    static {
        try (InputStream input = ConexionDB.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                System.err.println("No se encontro db.properties en resources.");
                System.err.println("Crea el archivo en src/main/resources/db.properties");
            } else {
                
                Properties props = new Properties();
                props.load(input);

                // extrae cada valor por su clave exacta
                url        = props.getProperty("db.url");
                usuario    = props.getProperty("db.usuario");
                contrasena = props.getProperty("db.contrasena");

                System.out.println("Configuracion de BD cargada correctamente.");
                System.out.println("Conectando a: " + url);
            }

        } catch (IOException e) {
            System.err.println("Error al leer db.properties: " + e.getMessage());
        }
    }
  
    private ConexionDB() {}

    //crea la conexion con los datos del archivo y devuelve la misma conexion ya creada
    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                
                Class.forName("oracle.jdbc.OracleDriver");

                conexion = DriverManager.getConnection(url, usuario, contrasena);
                System.out.println("Conexion exitosa a la base de datos Tienda naturista!");

            } catch (ClassNotFoundException e) {
                System.err.println("Driver de Oracle no encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error de conexion: " + e.getMessage());
            }
        }
        return conexion;
    }

    //se llama al cerrar la aplicacion
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexion cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
}