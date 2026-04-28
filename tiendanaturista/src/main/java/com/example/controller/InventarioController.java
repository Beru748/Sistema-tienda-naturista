package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * MOCK (Clase Falsa Temporal): 
 * Cuando el Dev 1 termine su clase Producto oficial en la carpeta "model", 
 * el Dev 2 solo tendrá que borrar esto de aquí e importar la verdadera.
 */
class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private String laboratorio;
    private int stock;
    private double precio;
    private String vencimiento;

    public Producto(int id, String nombre, String categoria, String laboratorio, int stock, double precio, String vencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.laboratorio = laboratorio;
        this.stock = stock;
        this.precio = precio;
        this.vencimiento = vencimiento;
    }

    // Getters obligatorios para que el TableView pueda leer la información
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getLaboratorio() { return laboratorio; }
    public int getStock() { return stock; }
    public double getPrecio() { return precio; }
    public String getVencimiento() { return vencimiento; }
}

public class InventarioController {

    /* ── Filtros ── */
    @FXML private TextField            campoBusqueda;
    @FXML private ComboBox<String>     comboCategoria;
    @FXML private ComboBox<String>     comboLaboratorio;
    @FXML private Button               btnLimpiarFiltros;

    /* ── Tabla ── */
    @FXML private TableView<Producto>              tablaInventario;
    @FXML private TableColumn<Producto, Integer>   colId;
    @FXML private TableColumn<Producto, String>    colNombre;
    @FXML private TableColumn<Producto, String>    colCategoria;
    @FXML private TableColumn<Producto, String>    colLaboratorio;
    @FXML private TableColumn<Producto, Integer>   colStock;
    @FXML private TableColumn<Producto, Double>    colPrecio;
    @FXML private TableColumn<Producto, String>    colVencimiento;

    /* ── Acciones ── */
    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private FilteredList<Producto> listaFiltrada;

    @FXML
    public void initialize() {
        // Enlaze de las columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colLaboratorio.setCellValueFactory(new PropertyValueFactory<>("laboratorio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("vencimiento"));

        // MOCKS: Datos de ejemplo para que la tabla se vea llena
        listaProductos.addAll(
            new Producto(1, "Spirulina 500mg",  "Suplementos", "NaturLab",  48, 32000.0, "2026-12-01"),
            new Producto(2, "Aceite de Coco",   "Aceites",     "BioNatural", 15, 28500.0, "2027-03-15"),
            new Producto(3, "Jengibre en polvo","Especias",    "HerbaCol",   30, 12000.0, "2026-08-20")
        );

        listaFiltrada = new FilteredList<>(listaProductos, p -> true);
        tablaInventario.setItems(listaFiltrada);

        comboCategoria.setItems(FXCollections.observableArrayList("Suplementos", "Aceites", "Especias", "Tés", "Cosméticos"));
        comboLaboratorio.setItems(FXCollections.observableArrayList("NaturLab", "BioNatural", "HerbaCol", "VidaSana"));

        campoBusqueda.textProperty().addListener((obs, old, nuevo) -> aplicarFiltros());
        comboCategoria.valueProperty().addListener((obs, old, nuevo) -> aplicarFiltros());
        comboLaboratorio.valueProperty().addListener((obs, old, nuevo) -> aplicarFiltros());

        btnLimpiarFiltros.setOnAction(e -> limpiarFiltros());
        btnAnadir.setOnAction(e   -> anadirProducto());
        btnEditar.setOnAction(e   -> editarProducto());
        btnEliminar.setOnAction(e -> eliminarProducto());
    }

    private void aplicarFiltros() {
        String texto = campoBusqueda.getText().toLowerCase().trim();
        String cat   = comboCategoria.getValue();
        String lab   = comboLaboratorio.getValue();

        listaFiltrada.setPredicate(p -> {
            boolean coincideTexto = texto.isEmpty() || p.getNombre().toLowerCase().contains(texto);
            boolean coincideCat  = cat == null || p.getCategoria().equals(cat);
            boolean coincideLab  = lab == null || p.getLaboratorio().equals(lab);
            return coincideTexto && coincideCat && coincideLab;
        });
    }

    private void limpiarFiltros() {
        campoBusqueda.clear();
        comboCategoria.setValue(null);
        comboLaboratorio.setValue(null);
    }

    private void anadirProducto() { mostrarAlerta("Añadir", "Aquí abrirás el formulario de nuevo producto."); }
    private void editarProducto() { mostrarAlerta("Editar", "Abre formulario para editar."); }
    
    private void eliminarProducto() {
        Producto seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado != null) listaProductos.remove(seleccionado);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}