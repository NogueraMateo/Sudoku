package com.example.sudokuproject.model;

import javafx.util.Pair;

import java.util.Random;

public class WelcomeSudokuModel extends NumberGenerator{

    public WelcomeSudokuModel() {}

    /**
     * Generates the indexes of a cell in the sudoku grid, by default
     * the indexes range from 0 to 5 in both row and column
     * @return A pair, the key is the column and the value is the row
     */
    public Pair<Integer, Integer> getRandomCell() {
        return getRandomCell(0, 5 ,0, 5);
    }

    /**
     * Generates the indexes of a cell in the sudoku grid
     * @param fromRow first row index
     * @param toRow last row index
     * @param fromCol first col index
     * @param toCol last col index
     * @return A pair, the key is the column and the value is the row
     */
    public Pair<Integer, Integer> getRandomCell(
            int fromCol,
            int toCol,
            int fromRow,
            int toRow) {

       return new Pair<>(this.generateNumber(fromCol, toCol), this.generateNumber(fromRow, toRow));
    }


}
