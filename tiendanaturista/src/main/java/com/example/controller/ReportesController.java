package com.example.controller; // ¡Paquete oficial de tu proyecto!

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

/**
 * MOCK (Clase Falsa Temporal):
 * Representa una factura de venta histórica. El Dev 2 reemplazará esto
 * por la clase real cuando la base de datos esté lista.
 */
class Factura {
    private String numFactura;
    private String fechaHora;
    private double totalFactura;

    public Factura(String numFactura, String fechaHora, double totalFactura) {
        this.numFactura = numFactura;
        this.fechaHora = fechaHora;
        this.totalFactura = totalFactura;
    }

    // Getters corregidos para que coincidan lógicamente con las variables
    public String getNumFactura() { return numFactura; }
    public String getFechaHora() { return fechaHora; }
    public double getTotalFactura() { return totalFactura; }
}

public class ReportesController {

    /* ── Filtros ── */
    @FXML private DatePicker fechaDesde;
    @FXML private DatePicker fechaHasta;
    @FXML private Button btnGenerarReporte;

    /* ── Tabla ── */
    @FXML private TableView<Factura> tablaReportes;
    @FXML private TableColumn<Factura, String> colNumFactura;
    @FXML private TableColumn<Factura, String> colFechaHora;
    @FXML private TableColumn<Factura, Double> colTotalFactura;

    /* ── Resumen ── */
    @FXML private Label labelTotalPeriodo;
    @FXML private Label labelCantidadFacturas;
    @FXML private Label labelPromedio;

    private final ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNumFactura.setCellValueFactory(new PropertyValueFactory<>("numFactura"));
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));

        tablaReportes.setItems(listaFacturas);

        // Fechas por defecto: del primer día del mes actual al día de hoy
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

        // SIMULACIÓN: la tabla tiene datos falsos de prueba al dar clic.
        // El Dev 2 borrará esto y hará el "SELECT" en Oracle.
        listaFacturas.setAll(
            new Factura("FAC-0001", "2026-04-01 09:14", 45000.0),
            new Factura("FAC-0002", "2026-04-03 11:32", 78500.0),
            new Factura("FAC-0003", "2026-04-10 16:05", 23000.0)
        );

        actualizarResumen();
    }

    private void actualizarResumen() {
        double total = listaFacturas.stream().mapToDouble(Factura::getTotalFactura).sum();
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