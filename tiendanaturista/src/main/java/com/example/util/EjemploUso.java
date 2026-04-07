package com.example.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EjemploUso {

    public void consultarDatos() {
        // Obtenemos la conexión llamando a nuestra clase
        Connection con = ConexionDB.obtenerConexion();
        
        String sql = "SELECT * FROM hr.employees";
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Leer datos por columna, ejemplo:
                // String nombre = rs.getString("NOMBRE_COLUMNA");
                // System.out.println(nombre);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta.");
            e.printStackTrace();
        }
        // No cerramos la conexión aquí si la vamos a seguir usando en otras partes de la aplicación
    }
}