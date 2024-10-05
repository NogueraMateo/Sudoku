package com.example.sudokuproject.controller;

import com.example.sudokuproject.model.SudokuGameModel;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
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
                textField.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);
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
            textField.getStyleClass().add("wrongCell");
            startWrongCellTransition(textField);
        } else {
            System.out.println("Took " + incomingNumber + " as a valid number");
        }
    }


    /**
     * When a key is released inside a cell I verify whether the text
     * field is empty. If it is then I remove the "wrongCell" css class
     * in case it has been set.
     * @param event: Retrieve the KeyEvent
     */
    public void handleKeyReleased(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        if (textField.getText().isEmpty()) {
            textField.getStyleClass().remove("wrongCell");
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


    public void startWrongCellTransition(TextField textField) {
        FadeTransition transition = new FadeTransition(Duration.seconds(1), textField);
        transition.setFromValue(0.3);
        transition.setToValue(1);
        transition.setCycleCount(FadeTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }
}
