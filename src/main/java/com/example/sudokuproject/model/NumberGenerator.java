package com.example.sudokuproject.model;

import java.util.Random;

/**
 * Utility class for generating random numbers within a specified range.
 * It uses an instance of {@link Random} initialized with the current system time.
 *
 * @author Mateo Noguera Pinto
 */
public class NumberGenerator {

    /**
     * The {@link Random} instance used to generate random numbers.
     */
    private final Random random;

    /**
     * Constructor for the NumberGenerator class.
     * Initializes the {@link Random} object with the current system time to ensure randomness.
     */
    public NumberGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    /**
     * Generates a random number between two integers, inclusive.
     *
     * @param n1 The lower bound of the range (inclusive).
     * @param n2 The upper bound of the range (inclusive).
     * @return A random integer between {@code n1} and {@code n2}, inclusive.
     */
    public int generateNumber(int n1, int n2) {
        return random.nextInt(n2 - n1 + 1) + n1;
    }

}
