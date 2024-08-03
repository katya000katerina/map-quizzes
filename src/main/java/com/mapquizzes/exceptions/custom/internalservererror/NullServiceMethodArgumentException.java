package com.mapquizzes.exceptions.custom.internalservererror;

public class NullServiceMethodArgumentException extends SimpleCustomInternalServerException{
    public NullServiceMethodArgumentException() {
        super("Method argument is null");
    }

    public NullServiceMethodArgumentException(String message) {
        super(message);
    }
}
