package com.example.sudokuproject;

import com.example.sudokuproject.view.WelcomeSudokuView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeSudoku extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        WelcomeSudokuView.getInstance();
    }
}
