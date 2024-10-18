package com.example.sudokuproject.controller;

import javafx.scene.control.TextField;

class TextFieldInfo {
    private final TextField field;
    private final int colIndex;
    private final int rowIndex;

    public TextFieldInfo(TextField field, int colIndex, int rowIndex) {
        this.field = field;
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }

    public TextField getCell() {
        return field;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}