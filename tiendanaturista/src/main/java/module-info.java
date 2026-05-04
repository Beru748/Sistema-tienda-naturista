module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    
    // iText
    requires kernel;
    requires layout;
    requires io;
    
    opens com.example to javafx.fxml;
    exports com.example;
    
    opens com.example.controller to javafx.fxml;
    exports com.example.controller;
}