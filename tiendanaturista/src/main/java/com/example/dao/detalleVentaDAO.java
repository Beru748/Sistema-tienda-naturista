package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.detalleVenta;
import com.example.util.ConexionDB;

public class detalleVentaDAO {

     // conexion privada a la base de datos
    private final Connection con = ConexionDB.obtenerConexion();

    // inserta un nuevo detalle de venta en la base de datos
    public boolean insertar(detalleVenta d) {
        String sql = "INSERT INTO detalle_venta VALUES (seq_detalle.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getIdVenta());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.setDouble(5, d.getSubTotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de venta: " + e.getMessage());
            return false;
        }
    }

    
}
