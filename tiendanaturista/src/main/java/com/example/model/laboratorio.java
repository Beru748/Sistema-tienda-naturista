package com.example.model;

public class laboratorio {
    int idLaboratorio;
    String nombre;
    String telefono;

    public laboratorio(int idLaboratorio, String nombre, String  telefono){
        this.idLaboratorio=idLaboratorio;
        this.nombre=nombre;
        this.telefono=telefono;
    }

    public int getIdLaboratorio(){
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratoio){
        this.idLaboratorio=idLaboratoio;
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

    @Override
    public String toString(){
        return "ID Laboratorio: "+idLaboratorio+"Nombre: "+nombre+"Telefono: "+telefono;

    }
}
