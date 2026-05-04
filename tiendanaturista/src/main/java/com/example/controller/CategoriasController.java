package com.example.controller;

import com.example.dao.categoriaDAO;
import com.example.model.categoria;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CategoriasController {

    //Tabla 
    @FXML private TableView<categoria>            tablaCategorias;
    @FXML private TableColumn<categoria, Integer> colCatId;
    @FXML private TableColumn<categoria, String>  colCatNombre;
    @FXML private TableColumn<categoria, String>  colCatDescripcion;

    //Formulario
    @FXML private TextField campoNombreCategoria;
    @FXML private TextField campoDescripcionCategoria;
    @FXML private Button    btnAnadirCategoria;
    @FXML private Button    btnEliminarCategoria;

    private final categoriaDAO dao = new categoriaDAO();
    private final ObservableList<categoria> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colCatId.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getIdCategoria()).asObject());
        colCatNombre.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNombre()));
        colCatDescripcion.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion()));

        tablaCategorias.setItems(lista);

        cargarCategorias();

        btnAnadirCategoria.setOnAction(e -> agregarCategoria());
        btnEliminarCategoria.setOnAction(e -> eliminarCategoria());
    }

    private void cargarCategorias() {
        lista.setAll(dao.listarTodos());
    }

    private void agregarCategoria() {
        String nombre      = campoNombreCategoria.getText().trim();
        String descripcion = campoDescripcionCategoria.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Campo requerido", "El nombre de la categoria es obligatorio.");
            return;
        }

        categoria cat = new categoria(0, nombre, descripcion);
        boolean ok    = dao.insertar(cat);

        if (ok) {
            cargarCategorias();
            campoNombreCategoria.clear();
            campoDescripcionCategoria.clear();
        } else {
            mostrarAlerta("Error", "No se pudo agregar la categoria.");
        }
    }

    private void eliminarCategoria() {
        categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Sin seleccion", "Selecciona una categoria para eliminar.");
            return;
        }

        boolean ok = dao.eliminar(seleccionada.getIdCategoria());

        if (ok) {
            cargarCategorias();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar. Puede tener productos asociados.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
