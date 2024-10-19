package com.example.sudokuproject.model;

import com.example.sudokuproject.model.exceptions.NoSuggestionFoundException;

import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Default constructor for SudokuGameModel.
     */
    public SudokuGameModel() {}

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
     * Validates whether a number can be placed in the current cell by checking
     * the numbers in the same column, row, and block.
     *
     * @param number The number to be validated.
     * @param colNums The list of numbers present in the column.
     * @param rowNums The list of numbers present in the row.
     * @param blockNums The list of numbers present in the 2x3 block.
     * @return True if the number is valid, false otherwise.
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
