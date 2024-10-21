package com.example.sudokuproject.model;

import com.example.sudokuproject.model.exceptions.NoSuggestionFoundException;

import java.util.*;

/**
 * SudokuGameModel extends {@link WelcomeSudokuModel} and adds functionality for validating
 * numbers and generating suggestions for the Sudoku game.
 *
 * This model is responsible for validating user input, ensuring that only valid numbers
 * are placed in the Sudoku grid, and providing suggestions when needed.
 *
 * @author Mateo Noguera Pinto
 */
public class SudokuGameModel extends WelcomeSudokuModel{

    private final int SIZE = 6;
    private final int[][] solvedBoard = new int[SIZE][SIZE];
    private final int[][] boardToShow = new int[SIZE][SIZE];


    /**
     * Default constructor for SudokuGameModel.
     */
    public SudokuGameModel() {
        generateBoard();
    }

    /**
     * Removes the necessary numbers from the solved board and
     * returns the result
     * @return int[][] : the board ready to be shown
     */
    public int[][] getBoardToShow() {
        removeNumbers(0, 0);
        removeNumbers(0, 3);
        removeNumbers(2, 0);
        removeNumbers(2, 3 );
        removeNumbers(4, 0);
        removeNumbers(4, 3);

        return boardToShow;
    }

    /**
     * Fills the board with random numbers while ensuring that each number
     * satisfies the Sudoku rules (no duplicates in the same row, column, or block).
     * If a valid configuration is found, it returns {@code true}.
     * If a number cannot be placed in a cell, it goes back and tries other numbers.
     *
     * @return {@code true} if the board is successfully generated, {@code false} otherwise.
    */
    public boolean generateBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                if (solvedBoard[row][col] == 0) {

                    List<Integer> numbers = getRandomNumbers();

                    for (int num : numbers) {

                        if (isValidNumber(num, row, col)) {
                            solvedBoard[row][col] = num;


                            if (generateBoard()) {
                                return true;
                            }

                            solvedBoard[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a shuffled list of numbers from 1 to the board size.
     * This ensures randomness in the Sudoku board generation.
     *
     * @return a shuffled {@code List} of integers from 1 to SIZE.
     */
    private List<Integer> getRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }


    /**
     * Validates if the provided character is a valid digit for the game.
     * A valid digit is between 1 and 6.
     *
     * @param letter The character to be validated.
     * @return True if the character is a valid digit between 1 and 6, false otherwise.
     */
    public boolean isValidChar(char letter) {
        if (Character.isDigit(letter)) {
            int digit = Character.getNumericValue(letter);
            return digit >= 1 && digit <= 6;
        }
        return false;
    }


    /**
     * Checks if a given number is valid in a specific cell of the Sudoku board.
     * The number is valid if it does not appear in the same row, column, or
     * 2x3 block as the specified cell.
     *
     * @param num the number to check.
     * @param row the row index of the cell to check.
     * @param col the column index of the cell to check.
     * @return {@code true} if the number is valid in the specified cell, {@code false} otherwise.
     */
    private boolean isValidNumber(int num, int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            if (solvedBoard[row][i] == num) {
                return false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (solvedBoard[i][col] == num) {
                return false;
            }
        }

        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;

        for (int i = blockRowStart; i < blockRowStart + 2; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if (solvedBoard[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Removes 4 random numbers from a specific 2x3 block of the Sudoku board.
     * The method ensures that exactly 4 cells are cleared (set to 0) within the block
     * defined by the starting row and column.
     *
     * @param fromRow the starting row of the 2x3 block.
     * @param fromCol the starting column of the 2x3 block.
     */
    public void removeNumbers(int fromRow, int fromCol) {
        Set<Integer> numsToRemove = new HashSet<>();
        while (numsToRemove.size() < 4) {
            int randNum = generateNumber(1, 6);
            numsToRemove.add(randNum);
        }

        for (int i = fromRow; i <= fromRow + 1; ++i) {
            for (int j = fromCol; j <= fromCol + 2; ++j) {
                if (numsToRemove.contains(solvedBoard[i][j])) {
                    boardToShow[i][j] = 0;
                    continue;
                }
                boardToShow[i][j] = solvedBoard[i][j];
            }
        }
    }

    /**
     * Compares the board passed with the solved one
     * @param board a matrix representing the current board of the user
     * @return
     */
    public boolean existsWinner(int[][] board) {
        return Arrays.deepEquals(board, solvedBoard);
    }


    /**
     * Checks if a given number is valid based on the provided lists of numbers
     * in the same row, column, and block. The number is valid if it does not
     * appear in any of these lists.
     *
     * @param number the number to check.
     * @param colNums the list of numbers present in the column.
     * @param rowNums the list of numbers present in the row.
     * @param blockNums the list of numbers present in the 2x3 block.
     * @return {@code true} if the number is valid, {@code false} if it is already present in one of the lists.
     */
    public boolean isValidNumber(
            int number,
            ArrayList<Integer> colNums,
            ArrayList<Integer> rowNums,
            ArrayList<Integer> blockNums) {

        return !colNums.contains(number) && !rowNums.contains(number) && !blockNums.contains(number);
    }


    /**
     * Provides a valid suggestion for an empty cell based on the numbers already
     * present in the same column, row, and block.
     *
     * @param numsInCol The list of numbers present in the column.
     * @param numsInRow The list of numbers present in the row.
     * @param numsInBlock The list of numbers present in the 2x3 block.
     * @return A valid suggestion (number) that can be placed in the empty cell.
     * @throws NoSuggestionFoundException If no valid suggestion can be found.
     */
    public int getSuggestion(
            ArrayList<Integer> numsInCol,
            ArrayList<Integer> numsInRow,
            ArrayList<Integer> numsInBlock
    ) throws NoSuggestionFoundException {
        Help help = new Help();
        return help.getSuggestion(numsInCol, numsInRow, numsInBlock);
    }


    /**
     * Inner helper class responsible for generating suggestions for the Sudoku grid.
     * The class keeps track of available numbers (1 to 6) and eliminates numbers
     * that are already present in the same row, column, or block.
     */
    class Help {

        /** List of available numbers for the suggestion. */
        private final ArrayList<Integer> availableNums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));


        /**
         * Generates a valid suggestion for a cell based on the numbers present
         * in the column, row, and block.
         *
         * @param numsInCol The list of numbers present in the column.
         * @param numsInRow The list of numbers present in the row.
         * @param numsInBlock The list of numbers present in the 2x3 block.
         * @return A valid number suggestion for the cell.
         * @throws NoSuggestionFoundException If no valid suggestion can be found.
         */
        public int getSuggestion(
                ArrayList<Integer> numsInCol,
                ArrayList<Integer> numsInRow,
                ArrayList<Integer> numsInBlock) throws NoSuggestionFoundException {

            // Remove numbers from the available list if they are present in the row
            for (int num : numsInRow) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            // Remove numbers from the available list if they are present in the column
            for (int num : numsInCol) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            // Remove numbers from the available list if they are present in the block
            for (int num : numsInBlock) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            // If there are no available numbers, throw an exception
            if (availableNums.isEmpty()) {
                throw new NoSuggestionFoundException();
            }

            // If only one number remains, return it
            if (availableNums.size() == 1) {
                return availableNums.get(0);
            }

            // Otherwise, generate a random valid suggestion from the remaining available numbers
            return availableNums.get(generateNumber(0, this.availableNums.size() - 1));
        }
    }
}
