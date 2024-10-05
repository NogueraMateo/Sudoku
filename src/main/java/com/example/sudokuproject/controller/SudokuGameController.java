package com.example.sudokuproject.controller;

import com.example.sudokuproject.model.SudokuGameModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

        this.fill2x3Block(0, 2, 0 , 1);
        this.fill2x3Block(3, 5, 0 , 1);
        this.fill2x3Block(0, 2, 2 , 3);
        this.fill2x3Block(3, 5, 2 , 3);
        this.fill2x3Block(0, 2, 4 , 5);
        this.fill2x3Block(3, 5, 4 , 5);
    }


    public void setWrongCell(int col, int row) {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                if (GridPane.getColumnIndex(textField).equals(col) && GridPane.getRowIndex(textField).equals(row)) {
                    textField.getStyleClass().add("wrongCell");
                }
            }
        }
    }


    public void removeWrongCell(int col, int row) {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                if (GridPane.getColumnIndex(textField) == col && GridPane.getRowIndex(textField) == row) {
                    textField.getStyleClass().remove("wrongCell");
                }
            }
        }
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
            return;
        }

        int incomingNumber = Integer.parseInt(incomingChar);

        Integer indexCol = GridPane.getColumnIndex(textField);
        Integer indexRow = GridPane.getRowIndex(textField);
        ArrayList<Integer> numbersInCol = getNumbersInColumn(indexCol);
        ArrayList<Integer> numbersInRow = getNumbersInRow(indexRow);

        if (!sudokuModel.isValidNumber(incomingNumber, numbersInCol)
                || !sudokuModel.isValidNumber(incomingNumber, numbersInRow)) {
            System.out.println("Invalid number");
            setWrongCell(indexCol, indexRow);
        } else {
            System.out.println("Took " + incomingNumber + " as a valid number");
            removeWrongCell(indexCol, indexRow);
        }
    }


    public void fill2x3Block(int fromCol, int toCol, int fromRow, int toRow) {
        Pair<Integer, Integer> randomCell;
        int randomNumber;
        ArrayList<Integer> numbersInRow = new ArrayList<>();
        ArrayList<Integer> numbersInCol = new ArrayList<>();
        Set<Integer> numbersInBlock = new HashSet<>();

        for (int i = 0; i < 2; ++i) {

            // Generates a random cell until the cell is empty
            do {
                randomCell = this.sudokuModel.getRandomCell(fromCol, toCol, fromRow, toRow);
            } while(!isCellEmpty(randomCell));

            // Gathering the numbers present along the column and row of the random cell
            numbersInCol = getNumbersInColumn(randomCell.getKey());
            numbersInRow = getNumbersInRow(randomCell.getValue());

            do {
                randomNumber = sudokuModel.generateNumber(0, 5);
            } while (!sudokuModel.isValidNumber(randomNumber, numbersInCol)
                    || !sudokuModel.isValidNumber(randomNumber, numbersInRow)
                    || numbersInBlock.contains(randomNumber));

            numbersInBlock.add(randomNumber);
            fillCell(randomCell, randomNumber);
        }
    }


    public void fillCell(Pair<Integer, Integer> cell, int number) {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                if (GridPane.getColumnIndex(textField).equals(cell.getKey()) &&
                        GridPane.getRowIndex(textField).equals(cell.getValue())) {
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);
                }

            }
        }
    }


    public boolean isCellEmpty(Pair<Integer, Integer> cell) {

        Integer colIndex, rowIndex;

        for (Node node : sudokuGrid.getChildren()) {

            if (node instanceof TextField textField) {

                colIndex = GridPane.getColumnIndex(node);
                rowIndex = GridPane.getRowIndex(node);

                if (colIndex.equals(cell.getKey()) && rowIndex.equals(cell.getValue())) {
                    return textField.getText().isEmpty();
                }

            }
        }
        return false;
    }


    public ArrayList<Integer> getNumbersInColumn(int col) {
        ArrayList<Integer> numbers = new ArrayList<>();
        Integer colIndex;

        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                colIndex = GridPane.getColumnIndex(textField);

                if (colIndex.equals(col) && !textField.getText().isEmpty()) {
                    numbers.add(Integer.parseInt(textField.getText()));
                }
            }
        }
        return numbers;
    }


    public ArrayList<Integer> getNumbersInRow(int row) {
        ArrayList<Integer> numbers = new ArrayList<>();
        Integer rowIndex;

        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField) {
                rowIndex = GridPane.getRowIndex(textField);

                if (rowIndex.equals(row) && !textField.getText().isEmpty()) {
                    numbers.add(Integer.parseInt(textField.getText()));
                }
            }
        }
        return numbers;
    }
}
