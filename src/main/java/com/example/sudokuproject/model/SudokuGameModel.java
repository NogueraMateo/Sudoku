package com.example.sudokuproject.model;

public class SudokuGameModel {

    public SudokuGameModel() {}

    public boolean isValidChar(char letter) {
        return Character.isDigit(letter);
    }
}
