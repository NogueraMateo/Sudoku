package com.example.sudokuproject.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeSudokuView extends Stage {


    public WelcomeSudokuView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(getClass().getResource(
                "/com/example/sudokuproject/welcome-view.fxml"
        ));
        Parent root = loader.load();
        this.setTitle("Welcome to Sudoku");
        Scene scene =  new Scene(root);
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }


    private static class WelcomeSudokuViewHolder {
        private static WelcomeSudokuView INSTANCE;
    }


    public static WelcomeSudokuView getInstance() throws IOException{

        if (WelcomeSudokuViewHolder.INSTANCE == null) {
            return WelcomeSudokuViewHolder.INSTANCE = new WelcomeSudokuView();
        } else {
            return WelcomeSudokuViewHolder.INSTANCE;
        }
    }

}
