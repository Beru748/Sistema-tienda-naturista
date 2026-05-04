package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

import com.example.model.detalleVenta;

public class ReportesController {

    // Filtros
    @FXML private DatePicker fechaDesde;
    @FXML private DatePicker fechaHasta;
    @FXML private Button btnGenerarReporte;

    //Tabla
    @FXML private TableView<detalleVenta> tablaReportes;
    @FXML private TableColumn<detalleVenta, String> colNumFactura;
    @FXML private TableColumn<detalleVenta, String> colFechaHora;
    @FXML private TableColumn<detalleVenta, Double> colTotalFactura;

    //Resumen
    @FXML private Label labelTotalPeriodo;
    @FXML private Label labelCantidadFacturas;
    @FXML private Label labelPromedio;

    private final ObservableList<detalleVenta> listaFacturas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNumFactura.setCellValueFactory(new PropertyValueFactory<>("idVenta")); 
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("idProducto")); 
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        tablaReportes.setItems(listaFacturas);

        //Fechas por defecto: del primer día del mes actual al día de hoy
        fechaDesde.setValue(LocalDate.now().withDayOfMonth(1));
        fechaHasta.setValue(LocalDate.now());

        btnGenerarReporte.setOnAction(e -> generarReporte());
    }

    private void generarReporte() {
        LocalDate desde = fechaDesde.getValue();
        LocalDate hasta = fechaHasta.getValue();

        if (desde == null || hasta == null) {
            mostrarAlerta("Fechas incompletas", "Selecciona ambas fechas para generar el reporte.");
            return;
        }
        if (desde.isAfter(hasta)) {
            mostrarAlerta("Rango inválido", "La fecha 'Desde' no puede ser posterior a 'Hasta'.");
            return;
        }

        actualizarResumen();
    }

    private void actualizarResumen() {
        double total = listaFacturas.stream().mapToDouble(detalleVenta::getSubTotal).sum();
        int cantidad = listaFacturas.size();
        double promedio = cantidad > 0 ? total / cantidad : 0.0;

        labelTotalPeriodo.setText(String.format("TOTAL VENDIDO EN EL PERIODO: $%,.2f", total));
        labelCantidadFacturas.setText(String.valueOf(cantidad));
        labelPromedio.setText(String.format("$%,.2f", promedio));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}