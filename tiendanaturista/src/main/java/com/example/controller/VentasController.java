package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class VentasController {

    /* ── Buscador ── */
    @FXML private TextField campoBusqueda;
    @FXML private Button btnAgregar;

    /* ── Tabla ── */
    @FXML private TableView<ProductoVenta> tablaVentas;
    @FXML private TableColumn<ProductoVenta, String> colProducto;
    @FXML private TableColumn<ProductoVenta, Integer> colCantidad;
    @FXML private TableColumn<ProductoVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<ProductoVenta, Double> colSubtotal;

    /* ── Resumen ── */
    @FXML private Label labelTotal;
    @FXML private Label labelCantidadProductos; 
    @FXML private Label labelTotalUnidades;  
    @FXML private Button btnProcesarVenta;
    @FXML private Button btnCancelar;

    // Esta es la lista que alimenta la tabla en tiempo real
    private final ObservableList<ProductoVenta> listaVenta = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlazar las columnas de la tabla con la clase ProductoVenta
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Conectar la lista a la tabla
        tablaVentas.setItems(listaVenta);

        btnAgregar.setOnAction(e -> agregarProducto());
        btnProcesarVenta.setOnAction(e -> procesarVenta());
        btnCancelar.setOnAction(e -> cancelarVenta());
    }

    private void agregarProducto() {
        String busqueda = campoBusqueda.getText().trim();
        if (busqueda.isEmpty()) return;

        // SIMULACIÓN: No importa qué escribas, agregará esto por ahora.
        // El Dev 2 cambiará esto para buscar en la BD real.
        ProductoVenta producto = new ProductoVenta(busqueda, 1, 15000.0);
        listaVenta.add(producto);
        
        campoBusqueda.clear();
        actualizarResumen();
    }

    private void actualizarResumen() {
        double total = listaVenta.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        // Formatea el texto para que se vea como dinero
        labelTotal.setText(String.format("TOTAL: $%,.2f", total));
        
        // Si no tienes estos labels en tu SceneBuilder, puedes borrar estas dos líneas
        if(labelCantidadProductos != null) labelCantidadProductos.setText(String.valueOf(listaVenta.size()));
        if(labelTotalUnidades != null) {
            int unidades = listaVenta.stream().mapToInt(ProductoVenta::getCantidad).sum();
            labelTotalUnidades.setText(String.valueOf(unidades));
        }
    }

    private void procesarVenta() {
        if (listaVenta.isEmpty()) {
            mostrarAlerta("Venta vacía", "Agrega al menos un producto antes de cobrar.");
            return;
        }
        
        // Aquí el Dev 2 hará el INSERT a la base de datos de Oracle
        mostrarAlerta("Venta Exitosa", "La venta ha sido procesada y registrada en el sistema.");
        cancelarVenta();
    }

    private void cancelarVenta() {
        listaVenta.clear();
        actualizarResumen();
        campoBusqueda.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}