package com.mapquizzes.exceptions.custom;

public class NullIdException extends RuntimeException{
    public NullIdException() {
        super("Id is null");
    }

    public NullIdException(String message) {
        super(message);
    }
}
