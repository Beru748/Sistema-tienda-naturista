package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    private producto mapear(ResultSet rs) throws SQLException {

        int     idProducto       = rs.getInt("id_producto");
        String  codigoBarras     = rs.getString("codigo_barras");
        String  nombre           = rs.getString("nombre");
        String  descripcion      = rs.getString("descripcion");
        double  precio           = rs.getDouble("precio");
        int     stock            = rs.getInt("stock");
        String  unidadMedida     = rs.getString("unidad_medida");
        Date    fecha            = rs.getDate("fecha_vencimiento");
        boolean activo           = rs.getInt("activo") == 1;
        int     idCategoria      = rs.getInt("id_categoria");
        int     idLaboratorio    = rs.getInt("id_laboratorio");

        producto p = new producto();
        p.setIdProducto(idProducto);
        p.setCodigoBarras(codigoBarras);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setUnidadMedida(unidadMedida);
        p.setFechaVencimiento(fecha != null ? fecha.toLocalDate() : null);
        p.setActivo(activo);
        p.setIdCategoria(idCategoria);
        p.setIdLaboratorio(idLaboratorio);
        return p;
    }

    //lista todos los productos activos
    public List<producto> listarTodos(){
        List<producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE activo = 1";
        try(Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(mapear(rs));
            }
        }catch(SQLException e){
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    //busca un producto por su ID
    public producto buscarPorId(int id){
        String sql = "SELECT * FROM WHERE id_producto = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapear(rs);
            }
        }catch(SQLException e){
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    //busca productos por nombre
    public List<producto> buscarPorNombre(String nombre){
        List<producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE UPPER(nombre) LIKE UPPER(?) AND activo = 1 ";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){

                lista.add(mapear(rs));
            }
        }catch(SQLException e){ 
            System.err.println("Error al buscar producto por nombre: " + e.getMessage());
        }
        return lista;
    }

    //busca productos por categoria
    public List<producto> buscarPorCategoria(int idCategoria){
        List<producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE id_categoria = ? AND activo = 1";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                lista.add(mapear(rs));
            }
        }catch(SQLException e){
            System.err.println("Error al buscar producto por categoria: " + e.getMessage());
        }
        return lista;
    }

    // actualiza un producto existente
    public boolean actualizar(producto p) {

        String sql = "UPDATE producto SET codigo_barras=?, nombre=?, descripcion=?, " +
                     "precio=?, stock=?, unidad_medida=?, fecha_vencimiento=?, " +
                     "id_categoria=?, id_laboratorio=? WHERE id_producto=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoBarras());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getUnidadMedida());
            ps.setDate(7, p.getFechaVencimiento() != null
                ? Date.valueOf(p.getFechaVencimiento()) : null);
            ps.setInt(8, p.getIdCategoria());
            ps.setInt(9, p.getIdLaboratorio());
            ps.setInt(10, p.getIdProducto());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    //"eliminar" - desactiva el producto sin borrar el registro
    public boolean eliminar(int id){

        String sql = "UPDATE producto SET activo = 0 WHERE id_producto = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

}
