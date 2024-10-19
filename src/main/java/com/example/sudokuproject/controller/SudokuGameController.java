package com.example.sudokuproject.controller;

import com.example.sudokuproject.controller.alerts.WinnerAlert;
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

/**
 * SudokuGameController is responsible for managing the logic and user interactions
 * in the Sudoku game. It handles the generation of the Sudoku board, user input,
 * and validations of the Sudoku grid cells.
 *
 * The controller uses animations to visually indicate invalid inputs, as well as
 * help the user by suggesting valid numbers for empty cells.
 *
 * @author Mateo Noguera Pinto
 */
public class SudokuGameController {

    private final SudokuGameModel sudokuModel;
    private Map<TextField, FadeTransition> wrongCells = new HashMap<>();
    @FXML
    private GridPane sudokuGrid;
    @FXML
    private Label noSuggestionLabel;

    /**
     * Default constructor initializes the SudokuGameModel.
     */
    public SudokuGameController() {
        this.sudokuModel = new SudokuGameModel();
    }

    /**
     * Initializes the Sudoku grid and sets event listeners for handling user input
     * when typing or releasing keys within the text fields.
     */
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

    /**
     * Handles the event when the help button is pressed. This method finds an empty
     * cell and provides a valid suggestion to the user.
     * If no suggestion can be found, it displays a message and an animation.
     */
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
        if (this.existsWinner()) {
            WinnerAlert alert = new WinnerAlert("Congratulations, you have won the game!");
            alert.showAndWait();
        }
    }

    /**
     * Generates a new Sudoku board by filling pre-determined 2x3 blocks with random numbers.
     */
    public void generateNewBoard() {
        fill2x3Block(0, 2, 0 , 1);
        fill2x3Block(3, 5, 0 , 1);
        fill2x3Block(0, 2, 2 , 3);
        fill2x3Block(3, 5, 2 , 3);
        fill2x3Block(0, 2, 4 , 5);
        fill2x3Block(3, 5, 4 , 5);
    }

    /**
     * Checks if the player has won the game by verifying that all cells are filled
     * and that there are no invalid entries.
     *
     * @return True if all cells are filled with valid numbers, false otherwise.
     */
    public boolean existsWinner() {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof TextField textField && (textField.getText().isEmpty() || this.wrongCells.containsKey(textField))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Handles the KeyEvent when a key is typed inside a text field. It validates
     * the input and ensures that only one number is entered per cell.
     *
     * @param event The KeyEvent triggered by typing inside the cell.
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
            textField.getStyleClass().add("wrongCell");
            startWrongCellTransition(textField);
        } else {
            this.stopWrongCellTransition(textField);
        }
    }


    /**
     * Handles the KeyEvent when a key is released inside a text field. It removes
     * the "wrongCell" CSS class if the field is empty and stops any running animations.
     *
     * @param event The KeyEvent triggered by releasing a key inside the cell.
     */
    public void handleKeyReleased(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        if (textField.getText().isEmpty()) {
            textField.getStyleClass().remove("wrongCell");
            this.stopWrongCellTransition(textField);
        }
        if (this.existsWinner()) {
            WinnerAlert alert = new WinnerAlert("Congratulations, you have won the game!");
            alert.showAndWait();
        }
    }

    /**
     * Fills a 2x3 block of cells within the specified column and row range with random valid numbers.
     *
     * @param fromCol The starting column index of the block.
     * @param toCol The ending column index of the block.
     * @param fromRow The starting row index of the block.
     * @param toRow The ending row index of the block.
     */
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

    /**
     * Fills the specified cell with the given number and marks the cell as non-editable.
     *
     * @param cell The cell represented by a pair of column and row indices.
     * @param number The number to fill in the cell.
     */
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

    /**
     * Checks if the specified cell is empty.
     *
     * @param cell The cell represented by a pair of column and row indices.
     * @return True if the cell is empty, false otherwise.
     */
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

    /**
     * Retrieves all numbers present in the specified column.
     *
     * @param col The index of the column.
     * @return A list of integers representing the numbers in the column.
     */
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

    /**
     * Retrieves all numbers present in the specified row.
     *
     * @param row The index of the row.
     * @return A list of integers representing the numbers in the row.
     */
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

    /**
     * Validates if the specified number can be placed in the given cell.
     * The method checks the numbers in the same row, column, and 2x3 block.
     *
     * @param digit The number to be validated.
     * @param cell The cell where the number is being placed, represented by a pair of column and row indices.
     * @return True if the number is valid, false otherwise.
     */
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

    /**
     * Retrieves all numbers present in the 2x3 block of cells.
     *
     * @param fromCol The starting column index of the block.
     * @param fromRow The starting row index of the block.
     * @return A list of integers representing the numbers in the block.
     */
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

    /**
     * Starts a fade transition on a text field, visually indicating that it contains an invalid value.
     * The transition makes the text field blink until corrected.
     *
     * @param textField The text field that contains an invalid value.
     */
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

    /**
     * Stops the fade transition for the specified text field, removing any visual indication that it contains an invalid value.
     *
     * @param textField The text field that no longer contains an invalid value.
     */
    public void stopWrongCellTransition(TextField textField) {
        if (this.wrongCells.containsKey(textField)) {
            this.wrongCells.get(textField).stop();
            this.wrongCells.remove(textField);
        }
    }

    /**
     * Retrieves all empty cells in the Sudoku grid.
     *
     * @return A list of {@link TextFieldInfo} objects representing the empty cells.
     */
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

    /**
     * Applies a fade transition to the specified label, gradually reducing its opacity.
     *
     * @param label The label to which the fade transition is applied.
     */
    public void LabelFadeTransition(Label label) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), label);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }
}