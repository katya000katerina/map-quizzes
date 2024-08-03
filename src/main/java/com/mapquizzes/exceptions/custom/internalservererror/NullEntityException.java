package com.mapquizzes.exceptions.custom.internalservererror;

public class NullEntityException extends SimpleCustomInternalServerException{
    public NullEntityException() {
        super("Entity is null");
    }

    public NullEntityException(String message) {
        super(message);
    }
}
