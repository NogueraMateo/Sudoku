module com.example.sudokuproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sudokuproject to javafx.fxml;
    exports com.example.sudokuproject;
    opens com.example.sudokuproject.controller to javafx.fxml;
    exports com.example.sudokuproject.controller;
}