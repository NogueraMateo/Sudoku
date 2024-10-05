package com.example.sudokuproject.model;

import java.util.Random;

public class NumberGenerator {

    private final Random random;

    public NumberGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public int generateNumber(int n1, int n2) {
        return random.nextInt(n2 - n1 + 1) + n1;
    }

}
