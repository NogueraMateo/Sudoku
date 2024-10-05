package com.example.sudokuproject.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuGameView extends Stage {

    public SudokuGameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/example/sudokuproject/game-view.fxml"
        ));

        Parent root = loader.load();
        this.setTitle("Sudoku Game");
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }


    private static class SudokuGameViewHolder{
        private static SudokuGameView INSTANCE;
    }


    public static SudokuGameView getInstance() throws IOException{

        if (SudokuGameViewHolder.INSTANCE == null){
            return SudokuGameViewHolder.INSTANCE = new SudokuGameView();
        } else {
            return SudokuGameViewHolder.INSTANCE;
        }

    }
}
