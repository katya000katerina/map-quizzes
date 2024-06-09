package com.mapquizzes.exceptions;

public class NullIdException extends RuntimeException{
    public NullIdException() {
        super("Id is null");
    }

    public NullIdException(String message) {
        super(message);
    }
}
