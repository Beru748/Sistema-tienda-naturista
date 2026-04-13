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

    

}
