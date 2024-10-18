package com.example.sudokuproject.controller;

import com.example.sudokuproject.model.SudokuGameModel;

import com.example.sudokuproject.model.exceptions.NoSuggestionFoundException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;

public class SudokuGameController {

    private final SudokuGameModel sudokuModel;
    private Map<TextField, FadeTransition> wrongCells = new HashMap<>();
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private Label noSuggestionLabel;


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


    @FXML
    public void onHelpPressed() {
        int colIndex, rowIndex, fromCol, fromRow, randEmptyCellNum, suggestion = 0;
        TextFieldInfo randomCell = null;
        ArrayList<TextFieldInfo> emptyCells = this.getEmptyCells();
        Set<Integer> triedCells = new HashSet<>();

        while(triedCells.size() <= emptyCells.size()) {
            try {
                if (emptyCells.size() == 1) {
                    randomCell = emptyCells.get(0);
                    triedCells.add(0);
                } else {
                    randEmptyCellNum = this.sudokuModel.generateNumber(0, emptyCells.size() - 1);
                    randomCell = emptyCells.get(randEmptyCellNum);
                    triedCells.add(randEmptyCellNum);
                }

                colIndex = randomCell.getColIndex();
                rowIndex = randomCell.getRowIndex();

                fromCol = (colIndex / 3) * 3;
                fromRow = (rowIndex / 2) * 2;

                suggestion = this.sudokuModel.getSuggestion(
                        this.getNumbersInColumn(colIndex),
                        this.getNumbersInRow(rowIndex),
                        this.getBlockNumbers(fromCol, fromRow)
                );
                break;
            } catch (NoSuggestionFoundException e) {
                if (triedCells.size() == emptyCells.size()) {
                    this.noSuggestionLabel.setVisible(true);
                    this.LabelFadeTransition(noSuggestionLabel);
                    return;
                }
            }
        }


        randomCell.getCell().setText(String.valueOf(suggestion));
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
            this.stopWrongCellTransition(textField);
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
            this.stopWrongCellTransition(textField);
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
        if (this.wrongCells.containsKey(textField)) {
            return;
        }
        FadeTransition transition = new FadeTransition(Duration.seconds(1), textField);
        transition.setFromValue(0.3);
        transition.setToValue(1);
        transition.setCycleCount(FadeTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        this.wrongCells.put(textField, transition);
    }

    public void stopWrongCellTransition(TextField textField) {
        if (this.wrongCells.containsKey(textField)) {
            this.wrongCells.get(textField).stop();
            this.wrongCells.remove(textField);
        }
    }


    public ArrayList<TextFieldInfo> getEmptyCells() {
        ArrayList<TextFieldInfo> emptyCells = new ArrayList<>();

        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField && textField.getText().isEmpty()) {

                emptyCells.add(new TextFieldInfo(
                        textField,
                        GridPane.getColumnIndex(textField),
                        GridPane.getRowIndex(textField)));

            }
        }
        return emptyCells;
    }

    public void LabelFadeTransition(Label label) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), label);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }
}