package com.example.sudokuproject.model;

import javafx.util.Pair;

import java.util.Random;

public class WelcomeSudokuModel implements NumberGenerator{

    private Random random;

    public WelcomeSudokuModel() {
        this.random = new Random();
    }

    @Override
    public int generateNumber(int n1, int n2) {
        return random.nextInt(n2 - n1 + 1) + n1;
    }

    public Pair<Integer, Integer> getRandomCell() {
       return new Pair<>(this.generateNumber(0, 5), this.generateNumber(0, 5));
    }


}
