package com.example.model;

public class cliente {
    int idCliente;
    String cedula;
    String nombre;
    String telefono;

    public cliente(int idCliente, String Cedula, String nombre, String telefono, String cedula){
        this.idCliente=idCliente;
        this.cedula=cedula;
        this.nombre=nombre;
        this.telefono=telefono;
    }

    public int getIdCliente(){
        return idCliente;
    }
    public void setIdCliente(int idCliente){
        this.idCliente=idCliente;
    }

    public String getCedula(){
        return cedula;
    }
    public void setCedula( String cedula){
        this.cedula=cedula;
    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public String getTelefono(){
        return telefono;
    }
    public void setTelefono(String telefono){
        this.telefono=telefono;
    }

}
