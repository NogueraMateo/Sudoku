package com.example.sudokuproject.model.exceptions;

public class NoSuggestionFoundException extends Exception {

    public NoSuggestionFoundException() {
        super();
    }

    public NoSuggestionFoundException(String message) {
        super(message);
    }

    public NoSuggestionFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuggestionFoundException(Throwable cause) {
        super(cause);
    }
}
