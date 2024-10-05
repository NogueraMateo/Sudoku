package com.example.sudokuproject.controller;

import com.example.sudokuproject.model.SudokuGameModel;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;

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

        generateNewBoard();
    }


    public void generateNewBoard() {
        fill2x3Block(0, 2, 0 , 1);
        fill2x3Block(3, 5, 0 , 1);
        fill2x3Block(0, 2, 2 , 3);
        fill2x3Block(3, 5, 2 , 3);
        fill2x3Block(0, 2, 4 , 5);
        fill2x3Block(3, 5, 4 , 5);
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

        if (!isValidNumber(incomingNumber, new Pair<>(indexCol, indexRow))) {
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

        for (int i = 0; i < 2; ++i) {

            // Generates a random cell until the cell is empty
            do {
                randomCell = this.sudokuModel.getRandomCell(fromCol, toCol, fromRow, toRow);
            } while(!isCellEmpty(randomCell));


            do {
                randomNumber = sudokuModel.generateNumber(1, 6);
            } while (!isValidNumber(randomNumber, randomCell));

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

    public boolean isValidNumber(
            int digit,
            Pair<Integer, Integer> cell) {

        // Gathering the numbers present along the column and row of the random cell
        ArrayList<Integer> numbersInCol = getNumbersInColumn(cell.getKey());
        ArrayList<Integer> numbersInRow = getNumbersInRow(cell.getValue());

        int fromCol = (cell.getKey() / 3) * 3;
        int fromRow = (cell.getValue() / 2) * 2;

        ArrayList<Integer> blockNums = getBlockNumbers(fromCol, fromRow);

        return sudokuModel.isValidNumber(digit, numbersInCol, numbersInRow, blockNums);
    }


    public ArrayList<Integer> getBlockNumbers(int fromCol, int fromRow) {
        ArrayList<Integer> numbers = new ArrayList<>();

        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField && !textField.getText().isEmpty()) {

                Integer colIndex = GridPane.getColumnIndex(textField);
                Integer rowIndex = GridPane.getRowIndex(textField);

                if (colIndex >= fromCol && colIndex <= (fromCol + 2) && rowIndex >= fromRow && rowIndex <= (fromRow + 1)) {
                    numbers.add(Integer.parseInt(textField.getText()));
                }
            }
        }

        return numbers;
    }
}
