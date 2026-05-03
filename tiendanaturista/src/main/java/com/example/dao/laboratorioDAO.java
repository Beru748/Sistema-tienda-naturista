package com.example.dao;

import java.sql.Connection;

import com.example.util.ConexionDB;

public class laboratorioDAO {
    
    //conexion privada a la base de datos
    private Connection con = ConexionDB.obtenerConexion();

}
