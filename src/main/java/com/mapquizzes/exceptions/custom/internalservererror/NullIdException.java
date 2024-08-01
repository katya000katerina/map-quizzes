package com.mapquizzes.exceptions.custom.internalservererror;

public class NullIdException extends SimpleCustomInternalServerException{
    public NullIdException() {
        super("Id is null");
    }

    public NullIdException(String message) {
        super(message);
    }
}
