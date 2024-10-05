package com.example.sudokuproject.model;

import java.util.ArrayList;

public class SudokuGameModel extends WelcomeSudokuModel{

    public SudokuGameModel() {}

    public boolean isValidChar(char letter) {
        if (Character.isDigit(letter)) {
            int digit = Character.getNumericValue(letter);
            return digit >= 1 && digit <= 6;
        }
        return false;
    }

    public boolean isValidNumber(int number, ArrayList<Integer> numbers) {
        return !numbers.contains(number);
    }
}
