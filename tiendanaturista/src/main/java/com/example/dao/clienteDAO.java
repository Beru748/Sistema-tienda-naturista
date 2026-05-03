package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.cliente;
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

    private cliente mapear(ResultSet rs) throws SQLException {
        int    idCliente = rs.getInt("id_cliente");
        String cedula    = rs.getString("cedula");
        String nombre    = rs.getString("nombre");
        String telefono  = rs.getString("telefono");
        return new cliente(idCliente, cedula, nombre, telefono);
    }

    
    //lista todos los clientes registrados
    public List<cliente> listarTodos(){
        List<cliente> lista = new ArrayList<>();

        String sql = "SELECT * FROM cliente";

        try(Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }catch(SQLException e){
                System.err.println("Error al listar clientes: " + e.getMessage());
            }
            return lista;
    }


}
