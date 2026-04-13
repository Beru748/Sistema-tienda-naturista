package com.example.model;

public class detalleVenta {
    int idDetalle;
    int idVenta;
    int idProducto;
    int cantidad;
    double precioUnitario;
    double subTotal;

    public detalleVenta(int idDetalle, int idVenta, int idProducto, int cantidad,double precioUnitario,double subTotal){
        this.idDetalle=idDetalle;
        this.idVenta=idVenta;
        this.idProducto=idProducto;
        this.cantidad=cantidad;
        this.precioUnitario=precioUnitario;
        this.subTotal=subTotal;
    }
    public int getIdDetalle(){
        return idDetalle;
    }
    public void setIdDetalle(int idDe   ){
        this.idDetalle=idDe;
    }

    public int getIdVenta(){
        return idVenta;
    }
    public void setIdVenta(int idVenta){
        this.idVenta=idVenta;
    }

    public int getIdProducto(){
        return idProducto;
    }
    public void setIdProducto(int idProducto){
        this.idProducto=idProducto;
    }

    public int getCantidad(){
        return cantidad;
    }
    public void setCantidad(int cantidad){
        this.cantidad=cantidad;
    }

    public double getPrecioUnitario(){
        return precioUnitario;
    }
    public void setPrecioUnitario(double precioUnitario){
        this.precioUnitario=precioUnitario;
    }

    public double getSubTotal(){
        return subTotal;
    }
    public void setSubTotal(double subTotal){
        this.subTotal=subTotal;
    }

    @Override
    public String toString(){
        return "ID Detalle: "+idDetalle+"ID Venta: "+idVenta+"ID Producto: "+idProducto+
        "Valor por Unidad: "+precioUnitario+"Subtotal: "+subTotal;
    }
    

}
