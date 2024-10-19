package com.example.sudokuproject.model;

import javafx.util.Pair;

import java.util.Random;

/**
 * The model for the Welcome Sudoku.
 * This class extends {@link NumberGenerator} to generate random numbers,
 * and provides methods to generate random cell indexes in the Sudoku grid.
 *
 * @author Mateo Noguera Pinto
 */
public class WelcomeSudokuModel extends NumberGenerator{

    /**
     * Default constructor for the model.
     * It initializes the model with no parameters.
     */
    public WelcomeSudokuModel() {}

    /**
     * Generates the indexes of a cell in the Sudoku grid.
     * By default, the indexes range from 0 to 5 for both the row and column.
     *
     * @return A {@link Pair} where the key is the column index and the value is the row index.
     */
    public Pair<Integer, Integer> getRandomCell() {
        return getRandomCell(0, 5 ,0, 5);
    }

    /**
     * Generates the indexes of a cell in the Sudoku grid within the specified range.
     *
     * @param fromCol The first column index.
     * @param toCol The last column index.
     * @param fromRow The first row index.
     * @param toRow The last row index.
     * @return A {@link Pair} where the key is the column index and the value is the row index.
     */
    public Pair<Integer, Integer> getRandomCell(
            int fromCol,
            int toCol,
            int fromRow,
            int toRow) {

       return new Pair<>(this.generateNumber(fromCol, toCol), this.generateNumber(fromRow, toRow));
    }


}
