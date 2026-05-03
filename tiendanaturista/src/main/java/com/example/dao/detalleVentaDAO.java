package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    // lista todos los detalles de una venta especifica
    public List<detalleVenta> listarPorVenta(int idVenta) {
        List<detalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE id_venta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar detalles de venta: " + e.getMessage());
        }
        return lista;
    }

    // mapeo ResultSet → objeto detalleVenta
    private detalleVenta mapear(ResultSet rs) throws SQLException {
        int    idDetalle      = rs.getInt("id_detalle");
        int    idVenta        = rs.getInt("id_venta");
        int    idProducto     = rs.getInt("id_producto");
        int    cantidad       = rs.getInt("cantidad");
        double precioUnitario = rs.getDouble("precio_unitario");
        double subTotal       = rs.getDouble("sub_total");
        return new detalleVenta(idDetalle, idVenta, idProducto, cantidad, precioUnitario, subTotal);
    }


}
