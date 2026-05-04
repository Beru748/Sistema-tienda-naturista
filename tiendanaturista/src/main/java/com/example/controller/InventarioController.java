package com.example.controller;

import java.io.IOException;
import java.util.List;

import com.example.dao.ProductoDAO;
import com.example.dao.categoriaDAO;
import com.example.dao.laboratorioDAO;
import com.example.model.categoria;
import com.example.model.laboratorio;
import com.example.model.producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventarioController {

    //Filtros
    @FXML private TextField        campoBusqueda;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<String> comboLaboratorio;
    @FXML private Button           btnLimpiarFiltros;

    //Tabla
    @FXML private TableView<producto>            tablaInventario;
    @FXML private TableColumn<producto, Integer> colId;
    @FXML private TableColumn<producto, String>  colNombre;
    @FXML private TableColumn<producto, Integer> colCategoria;
    @FXML private TableColumn<producto, Integer> colLaboratorio;
    @FXML private TableColumn<producto, Integer> colStock;
    @FXML private TableColumn<producto, Double>  colPrecio;
    @FXML private TableColumn<producto, String>  colVencimiento;

    //Acciones
    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnAbrirGestionCategorias;
    @FXML private Button btnAbrirGestionLaboratorios;

    //DAOs Oficiales
    private final categoriaDAO   catDAO = new categoriaDAO();
    private final laboratorioDAO labDAO = new laboratorioDAO();
    private final ProductoDAO    prodDAO = new ProductoDAO(); 

    private final ObservableList<producto> listaProductos = FXCollections.observableArrayList();
    private FilteredList<producto> listaFiltrada;

    @FXML
    public void initialize() {

        //Mapeo de columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colLaboratorio.setCellValueFactory(new PropertyValueFactory<>("idLaboratorio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));

        listaFiltrada = new FilteredList<>(listaProductos, p -> true);
        tablaInventario.setItems(listaFiltrada);

        // PROTECCIÓN CONTRA CAÍDAS DE BASE DE DATOS
        try {
            cargarComboCategoria();
            cargarComboLaboratorio();
            cargarProductosBD(); 
        } catch (Exception e) {
            System.err.println("Error crítico de BD al abrir Inventario: " + e.getMessage());
        }

        //Listeners de filtros
        campoBusqueda.textProperty().addListener((obs, old, nuevo) -> aplicarFiltros());
        comboCategoria.valueProperty().addListener((obs, old, nuevo) -> aplicarFiltros());
        comboLaboratorio.valueProperty().addListener((obs, old, nuevo) -> aplicarFiltros());

        //Botones
        btnLimpiarFiltros.setOnAction(e -> limpiarFiltros());
        btnAnadir.setOnAction(e -> anadirProducto());
        btnEditar.setOnAction(e -> editarProducto());
        btnEliminar.setOnAction(e -> eliminarProducto());
        btnAbrirGestionCategorias.setOnAction(e ->
            abrirVentanaGestion("GestionCategorias.fxml", "Gestionar Categorias"));
        btnAbrirGestionLaboratorios.setOnAction(e ->
            abrirVentanaGestion("GestionLaboratorios.fxml", "Gestionar Laboratorios"));
    }

    //Metodos de Carga de Datos Reales

    private void cargarProductosBD() {
        List<producto> productosBD = prodDAO.listarTodos();
        if (productosBD != null) {
            listaProductos.setAll(productosBD);
        }
    }

    private void cargarComboCategoria() {
        List<categoria> categorias = catDAO.listarTodos();
        if (categorias == null) return; 
        
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (categoria cat : categorias) {
            nombres.add(cat.getNombre());
        }
        comboCategoria.setItems(nombres);
    }

    private void cargarComboLaboratorio() {
        List<laboratorio> laboratorios = labDAO.listarTodos();
        if (laboratorios == null) return;
        
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (laboratorio lab : laboratorios) {
            nombres.add(lab.getNombre());
        }
        comboLaboratorio.setItems(nombres);
    }


    private void aplicarFiltros() {
        String texto = campoBusqueda.getText().toLowerCase().trim();
        listaFiltrada.setPredicate(p -> {
            return texto.isEmpty() || p.getNombre().toLowerCase().contains(texto);
        });
    }

    private void limpiarFiltros() {
        campoBusqueda.clear();
        comboCategoria.setValue(null);
        comboLaboratorio.setValue(null);
    }

    private void anadirProducto() {
        abrirVentanaGestion("GestionProducto.fxml", "Añadir Nuevo Producto");
    }

    private void editarProducto() {
        producto seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Por favor, selecciona un producto de la tabla para poder editarlo.");
            return;
        }
        abrirVentanaGestion("GestionProducto.fxml", "Editar Producto: " + seleccionado.getNombre());
    }

    private void eliminarProducto() {
        producto seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaProductos.remove(seleccionado);
            // NOTA: El Dev 2 deberá añadir aquí la lógica para borrar en Oracle:
            // prodDAO.eliminar(seleccionado.getIdProducto());
        }
    }

    private void abrirVentanaGestion(String fxmlFile, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refrescar todo al cerrar la ventana
            cargarComboCategoria();
            cargarComboLaboratorio();
            cargarProductosBD(); 

        } catch (IOException e) {
            System.err.println("Error al abrir " + fxmlFile + ": " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}