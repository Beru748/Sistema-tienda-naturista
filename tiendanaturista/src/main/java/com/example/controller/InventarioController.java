package com.example.controller;

import java.io.IOException;
import java.util.List;

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
    @FXML private TableColumn<producto, Integer>  colCategoria;
    @FXML private TableColumn<producto, Integer>  colLaboratorio;
    @FXML private TableColumn<producto, Integer> colStock;
    @FXML private TableColumn<producto, Double>  colPrecio;
    @FXML private TableColumn<producto, String>  colVencimiento;

    //Acciones
    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnAbrirGestionCategorias;
    @FXML private Button btnAbrirGestionLaboratorios;

    //DAOs para cargar los ComboBox desde la base de datos
    private final categoriaDAO   catDAO = new categoriaDAO();
    private final laboratorioDAO labDAO = new laboratorioDAO();

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

        //ComboBox desde la base de datos
        cargarComboCategoria();
        cargarComboLaboratorio();

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

    //Carga de ComboBox desde la base de datos

    private void cargarComboCategoria() {
        List<categoria> categorias = catDAO.listarTodos();
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (categoria cat : categorias) {
            nombres.add(cat.getNombre());
        }
        comboCategoria.setItems(nombres);
    }

    private void cargarComboLaboratorio() {
        List<laboratorio> laboratorios = labDAO.listarTodos();
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (laboratorio lab : laboratorios) {
            nombres.add(lab.getNombre());
        }
        comboLaboratorio.setItems(nombres);
    }

    //filtros

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

    //Acciones de productos

    private void anadirProducto() {
        mostrarAlerta("Añadir", "Aqui abriras el formulario de nuevo producto.");
    }

    private void editarProducto() {
        mostrarAlerta("Editar", "Abre formulario para editar.");
    }

    private void eliminarProducto() {
        producto seleccionado = tablaInventario.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaProductos.remove(seleccionado);
        }
    }

    //Ventanas de gestion

    private void abrirVentanaGestion(String fxmlFile, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/view/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // recarga combos al cerrar la ventana de gestion
            cargarComboCategoria();
            cargarComboLaboratorio();

        } catch (IOException e) {
            System.err.println("Error al abrir " + fxmlFile + ": " + e.getMessage());
        }
    }

    //Alertas

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}