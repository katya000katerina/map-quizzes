package com.mapquizzes.exceptions.custom.internalservererror;

public class SimpleCustomInternalServerException extends RuntimeException{
    public SimpleCustomInternalServerException() {
    }

    public SimpleCustomInternalServerException(String message) {
        super(message);
    }
}
