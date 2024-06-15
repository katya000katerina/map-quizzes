package com.mapquizzes.exceptions.custom;

public class NullDtoException extends RuntimeException{
    public NullDtoException() {
        super("DTO is null");
    }

    public NullDtoException(String message) {
        super(message);
    }
}
