package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Button btnCaja;
    @FXML private Button btnInventario;
    @FXML private Button btnReportes;
    @FXML private Button btnSalirMenu;

    private static final String ACTIVE_CLASS = "menu-button-active";

    @FXML
    public void initialize() {
        btnCaja.setOnAction(e      -> cargarVista("/com/example/view/Ventas.fxml",     btnCaja));
        btnInventario.setOnAction(e -> cargarVista("/com/example/view/Inventario.fxml", btnInventario));
        btnReportes.setOnAction(e  -> cargarVista("/com/example/view/Reportes.fxml",   btnReportes));
        btnSalirMenu.setOnAction(e     -> cerrarAplicacion());

    }

    private void cargarVista(String rutaFxml, Button botonActivo) {
        try {
            desmarcarTodos();

            botonActivo.getStyleClass().add(ACTIVE_CLASS);

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(rutaFxml)
            );
            Parent vista = loader.load();
            contentArea.getChildren().setAll(vista);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void desmarcarTodos() {
        btnCaja.getStyleClass().remove(ACTIVE_CLASS);
        btnInventario.getStyleClass().remove(ACTIVE_CLASS);
        btnReportes.getStyleClass().remove(ACTIVE_CLASS);
    }

    private void cerrarAplicacion() {
        Stage stage = (Stage) btnSalirMenu.getScene().getWindow();
        stage.close();
    }
}