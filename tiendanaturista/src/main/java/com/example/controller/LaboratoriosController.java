package com.example.controller;

import com.example.dao.laboratorioDAO;
import com.example.model.laboratorio;

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

public class LaboratoriosController {

    //Tabla
    @FXML private TableView<laboratorio>            tablaLaboratorios;
    @FXML private TableColumn<laboratorio, Integer> colLabId;
    @FXML private TableColumn<laboratorio, String>  colLabNombre;
    @FXML private TableColumn<laboratorio, String>  colLabTelefono;

    //Formulario
    @FXML private TextField campoNombreLaboratorio;
    @FXML private TextField campoTelefonoLaboratorio;
    @FXML private Button    btnAnadirLaboratorio;
    @FXML private Button    btnEliminarLaboratorio;

    private final laboratorioDAO dao = new laboratorioDAO();
    private final ObservableList<laboratorio> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colLabId.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getIdLaboratorio()).asObject());
        colLabNombre.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNombre()));
        colLabTelefono.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTelefono()));

        tablaLaboratorios.setItems(lista);

        //carga los laboratorios desde la base de datos al abrir la ventana
        cargarLaboratorios();

        btnAnadirLaboratorio.setOnAction(e -> agregarLaboratorio());
        btnEliminarLaboratorio.setOnAction(e -> eliminarLaboratorio());
    }

    private void cargarLaboratorios() {
        lista.setAll(dao.listarTodos());
    }

    private void agregarLaboratorio() {
        String nombre   = campoNombreLaboratorio.getText().trim();
        String telefono = campoTelefonoLaboratorio.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Campo requerido", "El nombre del laboratorio es obligatorio.");
            return;
        }

        laboratorio lab = new laboratorio(0, nombre, telefono);
        boolean ok      = dao.insertar(lab);

        if (ok) {
            cargarLaboratorios();
            campoNombreLaboratorio.clear();
            campoTelefonoLaboratorio.clear();
        } else {
            mostrarAlerta("Error", "No se pudo agregar el laboratorio.");
        }
    }

    private void eliminarLaboratorio() {
        laboratorio seleccionado = tablaLaboratorios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Sin seleccion", "Selecciona un laboratorio para eliminar.");
            return;
        }

        boolean ok = dao.eliminar(seleccionado.getIdLaboratorio());

        if (ok) {
            cargarLaboratorios();
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
