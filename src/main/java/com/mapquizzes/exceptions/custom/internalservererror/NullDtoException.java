package com.mapquizzes.exceptions.custom.internalservererror;

public class NullDtoException extends SimpleCustomInternalServerException{
    public NullDtoException() {
        super("DTO is null");
    }

    public NullDtoException(String message) {
        super(message);
    }
}
