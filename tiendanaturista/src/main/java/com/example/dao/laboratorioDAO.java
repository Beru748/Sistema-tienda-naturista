package com.example.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.laboratorio;
import com.example.util.ConexionDB;

public class laboratorioDAO {

    //conexion privada a la base de datos
    private Connection con = ConexionDB.obtenerConexion();

     //inserta un nuevo laboratorio a la abse de datos
     public boolean insertar (laboratorio lab){
        String sql = "INSERT INTO laboratoio VALUES (seq_laboratorio.NEXTVAL, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lab.getNombre());
            ps.setString(2, lab.getTelefono());
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            System.err.println("Error al insertar laboratorio");
            e.printStackTrace();
            return false;
        }

     }

}
