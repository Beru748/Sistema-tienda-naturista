package com.example.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
            System.err.println("Error al insertar la categoria: " + e.getMessage());
            return false;
        }
    }

    //lista todas laas categorias registradas
    public List<categoria> listarTodos(){
        List<categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        
        try(Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }catch(SQLException e){
                System.err.println("Error al listar las categorias: " + e.getMessage());
            }
            return lista;

    }

    private categoria mapear(ResultSet rs) throws SQLException{
        
        int idCategoria = rs.getInt("id_categoria");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");

        return new categoria(idCategoria, nombre, descripcion);
    }

}
