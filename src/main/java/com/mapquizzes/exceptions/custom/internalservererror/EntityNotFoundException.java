package com.mapquizzes.exceptions.custom.internalservererror;

public class EntityNotFoundException extends SimpleCustomInternalServerException {
    public EntityNotFoundException() {
        super("Entity not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
