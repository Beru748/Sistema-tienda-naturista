package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.venta;
import com.example.util.ConexionDB;

public class ventaDAO {
    // conexion privada a la base de datos
    private final Connection con = ConexionDB.obtenerConexion();

    // inserta una nueva venta en la base de datos
    public boolean insertar(venta v) {
        String sql = "INSERT INTO venta VALUES (seq_venta.NEXTVAL, CURRENT_TIMESTAMP, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, v.getTotal());
            ps.setString(2, v.getMetodoPago());
            ps.setInt(3, v.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }
}
