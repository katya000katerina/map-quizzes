package com.mapquizzes.exceptions.custom;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException() {
        super("Invalid id");
    }

    public InvalidIdException(String message) {
        super(message);
    }
}
