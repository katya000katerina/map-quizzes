package com.mapquizzes.exceptions.custom.badrequest;

public class SimpleCustomBadRequestException extends RuntimeException{
    public SimpleCustomBadRequestException() {
    }

    public SimpleCustomBadRequestException(String message) {
        super(message);
    }
}
