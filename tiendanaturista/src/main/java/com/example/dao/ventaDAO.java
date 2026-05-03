package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private venta mapear(ResultSet rs) throws SQLException{
        int idVenta       = rs.getInt("id_venta");
        double total      = rs.getDouble("total");
        String metodoPago = rs.getString("metodo_pago");
        int idCliente     = rs.getInt("id_cliente");
        
        return new venta(idVenta,
            rs.getTimestamp("fecha_hora").toLocalDateTime(),
            total,
            metodoPago,
            idCliente
        );
    }

    //lista todas las ventas ordenadas por fecha
    public List<venta> listarTodos(){
        List<venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta ORDER BY fecha_hora DESC";
        try(Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }catch(SQLException e){
                System.err.println("Error al listar ventas: " + e.getMessage());
            }
            return lista;
    }

    //busca una venta por su ID
    public venta buscarPorId(int id){
        String sql = "SELECT * FROM venta WHERE id_venta = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapear(rs);
            }
        }catch(SQLException e){
            System.err.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }

    // busca ventas entre dos fechas
    public List<venta> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        List<venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta WHERE TRUNC(fecha_hora) BETWEEN ? AND ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ventas por rango de fechas: " + e.getMessage());
        }
        return lista;
    }
    
}
