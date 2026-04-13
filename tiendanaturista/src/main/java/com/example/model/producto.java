package com.example.model;

import java.time.LocalDate;

public class producto {
    int idProducto;
    String codigoBarras;
    String nombre;
    String descripcion;
    double precio;
    int stock;
    String unidadMedida;
    LocalDate fechaVencimiento;
    boolean activo;
    int idCategoria;
    int idLaboratorio;

    public producto(){
        this.activo=true;
    }

    public int getIdProducto(){
        return idProducto;
    }
    public void setIdProducto(int idProducto){
        this.idProducto=idProducto;
    }

    public String getCodigoBarras(){
        return codigoBarras;
    }
    public void setCodigoBarras(String codigoBarras){
        this.codigoBarras=codigoBarras;
    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descString){
        this.descripcion=descString;
    }

    public double getPrecio(){
        return precio;
    }
    public void setPrecio(double precio){
        this.precio=precio;
    }

    public int getStock(){
        return stock;
    }
    public void setStock(int stock){
        this.stock=stock;
    }

    public String getUnidadMedida(){
        return unidadMedida;
    }
    public void setUnidadMedida(String unidadMString){
        this.unidadMedida=unidadMString;
    }

    public LocalDate getFechaVencimiento(){
        return fechaVencimiento;
    }
    public void setFechaVencimiento( LocalDate fechaVenDate){
        this.fechaVencimiento=fechaVenDate;
    }

    public boolean getActivo(){
        return activo;
    }
    public void setActivo(boolean activo){
        this.activo=activo;
    }

    public int getIdCategoria(){
        return idCategoria;
    }
    public void setIdCategotia(int idCategoria){
        this.idCategoria=idCategoria;
    }

    public int getIdLaboratorio(){
        return idLaboratorio;
    }
    public void setIdLaboratorio(int idLaboratorio){
        this.idLaboratorio=idLaboratorio;
    }

}
