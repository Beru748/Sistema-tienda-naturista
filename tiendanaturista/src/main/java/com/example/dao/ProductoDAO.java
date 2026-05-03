package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.producto;
import com.example.util.ConexionDB;

public class ProductoDAO {

    // conexion privada a la base de datos
    private final Connection con = ConexionDB.obtenerConexion();

    // inserta un nuevo producto en la base de datos
    public boolean insertar(producto p) {

        String sql = "INSERT INTO producto VALUES (seq_producto.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoBarras());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getUnidadMedida());
            ps.setDate(7, p.getFechaVencimiento() != null
                ? Date.valueOf(p.getFechaVencimiento()) : null);
            ps.setInt(8, p.getActivo() ? 1 : 0);
            ps.setInt(9, p.getIdCategoria());
            ps.setInt(10, p.getIdLaboratorio());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
}
