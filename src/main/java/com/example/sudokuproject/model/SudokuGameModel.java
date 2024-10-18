package com.example.sudokuproject.model;

import com.example.sudokuproject.model.exceptions.NoSuggestionFoundException;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuGameModel extends WelcomeSudokuModel{

    public SudokuGameModel() {}


    public boolean isValidChar(char letter) {
        if (Character.isDigit(letter)) {
            int digit = Character.getNumericValue(letter);
            return digit >= 1 && digit <= 6;
        }
        return false;
    }


    public boolean isValidNumber(
            int number,
            ArrayList<Integer> colNums,
            ArrayList<Integer> rowNums,
            ArrayList<Integer> blockNums) {

        return !colNums.contains(number) && !rowNums.contains(number) && !blockNums.contains(number);
    }

    public int getSuggestion(
            ArrayList<Integer> numsInCol,
            ArrayList<Integer> numsInRow,
            ArrayList<Integer> numsInBlock
    ) throws NoSuggestionFoundException {
        Help help = new Help();
        return help.getSuggestion(numsInCol, numsInRow, numsInBlock);
    }

    class Help {
        private final ArrayList<Integer> availableNums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        public int getSuggestion(
                ArrayList<Integer> numsInCol,
                ArrayList<Integer> numsInRow,
                ArrayList<Integer> numsInBlock) throws NoSuggestionFoundException {

            for (int num : numsInRow) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            for (int num : numsInCol) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            for (int num : numsInBlock) {
                if (this.availableNums.contains(num)) {
                    availableNums.remove((Integer) num);
                }
            }

            System.out.println(availableNums);
            if (availableNums.isEmpty()) {
                throw new NoSuggestionFoundException();
            }
            if (availableNums.size() == 1) {
                return availableNums.get(0);
            }
            return availableNums.get(generateNumber(0, this.availableNums.size() - 1));
        }
    }
}
