package com.example.controller;
import java.util.Optional;
import com.example.model.producto;
import com.example.dao.ProductoDAO;
import com.example.model.venta;
import java.time.LocalDateTime;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

class ProductoVenta {
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public ProductoVenta(String nombre, int cantidad, double precioUnitario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
}

public class VentasController {

    //Buscador
    @FXML private TextField campoBusqueda;
    @FXML private Button btnAgregar;

    //Tabla
    @FXML private TableView<ProductoVenta> tablaVentas;
    @FXML private TableColumn<ProductoVenta, String> colProducto;
    @FXML private TableColumn<ProductoVenta, Integer> colCantidad;
    @FXML private TableColumn<ProductoVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<ProductoVenta, Double> colSubtotal;

    //Resumen
    @FXML private Label labelTotal;
    @FXML private Label labelCantidadProductos; 
    @FXML private Label labelTotalUnidades;  
    @FXML private Button btnProcesarVenta;
    @FXML private Button btnCancelar;

    // La lista ahora maneja objetos ProductoVenta
    private final ObservableList<ProductoVenta> listaVenta = FXCollections.observableArrayList();
    private final ProductoDAO productoDAO = new ProductoDAO();

    @FXML
    public void initialize() {
        // Enlazar las columnas de la tabla con la clase ProductoVenta
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tablaVentas.setItems(listaVenta);

        btnAgregar.setOnAction(e -> agregarProducto());
        btnProcesarVenta.setOnAction(e -> procesarVenta());
        btnCancelar.setOnAction(e -> cancelarVenta());
    }

    private void agregarProducto() {
        String busqueda = campoBusqueda.getText().trim();
        if (busqueda.isEmpty()) return;

        java.util.List<producto> resultados = productoDAO.buscarPorNombre(busqueda);

        if (resultados == null || resultados.isEmpty()) {
            mostrarAlerta("No encontrado", "No se encontró el producto en el inventario.");
            return;
        }

        producto productoReal = resultados.get(0);

        TextInputDialog dialog = new TextInputDialog("1"); 
        dialog.setTitle("Cantidad a vender");
        dialog.setHeaderText("Producto: " + productoReal.getNombre() + "\nPrecio: $" + productoReal.getPrecio());
        dialog.setContentText("¿Cuántas unidades desea agregar?");

        Optional<String> resultado = dialog.showAndWait();
        
        if (resultado.isPresent()) {
            try {
                int cantidadPedida = Integer.parseInt(resultado.get());

                if (cantidadPedida > productoReal.getStock()) {
                    mostrarAlerta("Stock insuficiente", "Solo hay " + productoReal.getStock() + " unidades en bodega.");
                    return;
                }

                if (cantidadPedida > 0) {
                    ProductoVenta nuevoProducto = new ProductoVenta(
                            productoReal.getNombre(), 
                            cantidadPedida, 
                            productoReal.getPrecio()
                    );
                    
                    listaVenta.add(nuevoProducto);
                    campoBusqueda.clear();
                    actualizarResumen();
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Por favor, ingrese un número válido.");
            }
        }
    }

    private void actualizarResumen() {
        double total = listaVenta.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        labelTotal.setText(String.format("TOTAL: $%,.2f", total));
        
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
        
        double totalFactura = listaVenta.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        
        venta nuevaVenta = new venta(0, LocalDateTime.now(), totalFactura, "Efectivo", 1);

        mostrarAlerta("Venta Exitosa", "Factura registrada por un total de: $" + nuevaVenta.getTotal());
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