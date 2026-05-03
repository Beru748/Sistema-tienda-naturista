package com.example.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.categoria;
import com.example.util.ConexionDB;

public class categoriaDAO {

    //conexion privada a la base de datos
    private final Connection con = ConexionDB.obtenerConexion();

    //insertar una nueva categoria en la base de datos
    public boolean insertar(categoria cat){

        String sql = "INSERT INTO categoria VALUES (seq_categoria.NEXTVAL, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            return ps.executeUpdate() > 0;

        }catch(SQLException e){
            System.err.println("Error al insertar categoria: " + e.getMessage());
            return false;
        }
    }

}
