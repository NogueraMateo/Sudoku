package com.example.sudokuproject.controller;

import javafx.scene.control.TextField;

/**
 * TextFieldInfo is a helper class that holds information about a {@link TextField}
 * and its position in a grid, represented by column and row indices.
 *
 * This class provides methods to retrieve the text field and its grid position.
 *
 * @author Mateo Noguera Pinto
 */
class TextFieldInfo {

    /** The TextField instance associated with the cell in the grid. */
    private final TextField field;

    /** The column index of the TextField in the grid. */
    private final int colIndex;

    /** The row index of the TextField in the grid. */
    private final int rowIndex;

    /**
     * Constructs a TextFieldInfo object that holds a {@link TextField} and its
     * position in a grid.
     *
     * @param field The TextField object.
     * @param colIndex The column index where the TextField is located.
     * @param rowIndex The row index where the TextField is located.
     */
    public TextFieldInfo(TextField field, int colIndex, int rowIndex) {
        this.field = field;
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }

    /**
     * Returns the {@link TextField} associated with this object.
     *
     * @return The TextField instance.
     */
    public TextField getCell() {
        return field;
    }

    /**
     * Returns the column index of the TextField in the grid.
     *
     * @return The column index.
     */
    public int getColIndex() {
        return colIndex;
    }

    /**
     * Returns the row index of the TextField in the grid.
     *
     * @return The row index.
     */

    public int getRowIndex() {
        return rowIndex;
    }
}