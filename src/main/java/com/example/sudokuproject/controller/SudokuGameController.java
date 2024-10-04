package com.example.sudokuproject.controller;

import com.example.sudokuproject.model.SudokuGameModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class SudokuGameController {

    private final SudokuGameModel sudokuModel;

    @FXML
    private GridPane sudokuGrid;

    public SudokuGameController() {
        this.sudokuModel = new SudokuGameModel();
    }

    @FXML
    public void initialize() {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                textField.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped);
            }
        }
    }

    public void setWrongCell(int row, int col) {
        return;
    }

    /**
     * The goal of this method is to forbid the input of more than one
     * number by cell
     */
    public void handleKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String incomingChar = event.getCharacter();
        String currentChar = textField.getText();

        if (!currentChar.isEmpty() || !this.sudokuModel.isValidChar(incomingChar.charAt(0)))  {
            event.consume();
        }
    }

}
