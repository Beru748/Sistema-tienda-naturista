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

    public String getMetodoPago(){
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago){
        this.metodoPago=metodoPago;
    }


    public int getIdCliente(){
        return idCliente;
    }
    public void setIdCliente(int idCliente){
        this.idCliente=idCliente;
    }

    @Override
    public String toString(){
        return "ID Venta: "+idVenta+"Fecha y Hora de la Venta: "+fechaHora+
        "Total de la Venta: "+total+"Metodo de Pago: "+metodoPago+"ID Cliente: "+idCliente;
    }
    


}
