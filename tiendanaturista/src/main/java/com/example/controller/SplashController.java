package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SplashController {

    @FXML 
    private Button btnIngresar;

    @FXML
    public void initialize() {
        btnIngresar.setOnAction(e -> abrirDashboard());
    }

    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/view/Dashboard.fxml") 
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.setTitle("Natural & Belleza — Dashboard");
            stage.show();
            
        } catch (Exception ex) {
            System.out.println("Error al cargar la pantalla: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}