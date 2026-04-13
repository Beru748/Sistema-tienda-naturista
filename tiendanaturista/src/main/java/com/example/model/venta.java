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
    


}
