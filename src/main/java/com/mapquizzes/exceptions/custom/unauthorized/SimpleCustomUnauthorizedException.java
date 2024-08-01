package com.mapquizzes.exceptions.custom.unauthorized;

public class SimpleCustomUnauthorizedException extends RuntimeException{
    public SimpleCustomUnauthorizedException() {
    }

    public SimpleCustomUnauthorizedException(String message) {
        super(message);
    }
}
