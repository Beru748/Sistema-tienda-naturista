package com.example.model;

import java.time.LocalDateTime;

public class venta {
    int idVenta;
    LocalDateTime fechaHora;
    double total;
    String metodoPago;
    int idCliente;

    public venta(int idVenta, LocalDateTime fechaHora, double total, String metodoPago, int idCliente){
        this.idVenta=idVenta;
        this.fechaHora=fechaHora;
        this.total=total;
        this.idCliente=idCliente;
    }
    public int getIdVenta(){
    return idVenta;
    }
    public void setIdVenta(int idVenta){
      this.idVenta=idVenta;
    }

    public LocalDateTime getFechaHora(){
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora){
       this.fechaHora=fechaHora;
    }

    public double getTotal(){
        return total;
    }
    public void setTotal(double total){
        this.total=total;
    }
    


}
