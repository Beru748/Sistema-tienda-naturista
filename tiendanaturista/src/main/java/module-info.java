module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;

    opens com.example to javafx.fxml;
    exports com.example;

    opens com.example.controller to javafx.fxml;
    exports com.example.controller;
}