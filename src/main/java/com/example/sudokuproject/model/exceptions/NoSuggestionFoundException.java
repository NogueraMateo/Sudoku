package com.example.sudokuproject.model.exceptions;

/**
 * Exception thrown when no valid suggestion can be found for a cell in the Sudoku game.
 *
 * This exception is typically used when there are no available numbers that can be placed
 * in a Sudoku cell without violating the game rules.
 *
 * @author Mateo Noguera Pinto
 */
public class NoSuggestionFoundException extends Exception {

    /**
     * Constructs a new {@code NoSuggestionFoundException} with {@code null} as its detail message.
     * The cause is not initialized and may be initialized later by calling {@link Throwable#initCause}.
     */
    public NoSuggestionFoundException() {
        super();
    }

    /**
     * Constructs a new {@code NoSuggestionFoundException} with the specified detail message.
     * The cause is not initialized and may be initialized later by calling {@link Throwable#initCause}.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public NoSuggestionFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code NoSuggestionFoundException} with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method.
     * @param cause The cause, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public NoSuggestionFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code NoSuggestionFoundException} with the specified cause.
     *
     * @param cause The cause, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public NoSuggestionFoundException(Throwable cause) {
        super(cause);
    }
}
