package com.example.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
      
    private laboratorio mapear(ResultSet rs) throws SQLException{

        int idLaboratorio = rs.getInt("id_laboratorio");
        String nombre     = rs.getString("nombre");
        String telefono   = rs.getString("telefono");

        return new laboratorio(idLaboratorio, nombre, telefono);
    }

    //lista todos los laboratorios
    public List<laboratorio> listarTodos(){
        List<laboratorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM laboratorio";

        try(Statement st = con.createStatement();
            ResultSet rs = sr.executeQuery(sql)){
                while (rs.next()){
                    lista.add(mapear(rs));
                }
            }catch (SQLException e){
                System.err.println("Error al listar laboratorio");
                e.printStackTrace();
            }
            return lista;
    }

    //Buscar laboratorio por id
    public laboratorio buscarPorId(int id) {
        String sql = "SELECT * FROM laboratorio WHERE id_laboratorio = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar laboratorio.");
            e.printStackTrace();
        }
        return null;
    }

}
