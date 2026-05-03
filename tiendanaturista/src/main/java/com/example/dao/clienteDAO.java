package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.util.ConexionDB;
public class clienteDAO {

    //conexion privada a la base de datos
    private final Connection con = ConexionDB.obtenerConexion();

    //inserta un nuevo cliente en la base de datos
    public boolean insertar(cliente cli){
        String sql = "INSERT INTO cliente VALUES (seq_cliente.NEXTVAL, ?, ?, ?)";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, cli.getCedula());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getTelefono());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error al insertar cliente: "+ e.getMessage());
            return false;
        }
    }


}
