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

    //lista todas las categorias registradas
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

    //busca una categoria por ID
    public categoria buscarPorId(int id){

        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapear(rs);
            }
        }catch(SQLException e){
            System.err.println("Error al buscar categoria: "+ e.getMessage());
        }
        return null;

    }

    //actualiza una categoria registrada por su ID
    public boolean actualizar(categoria cat){
        String sql = "UPDATE categoria SET nombre=?, descripcion=? WHERE id_categoria=?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            ps.setInt(3, cat.getIdCategoria());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error al actualizar la categoria: "+ e.getMessage());
            return false;
        }
    }

    //elimina una categoria por su ID
    public boolean eliminar(int id){
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error al eliminar la categoria: "+ e.getLocalizedMessage());
            return false;
        }
    }

    

}
