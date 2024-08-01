package com.mapquizzes.exceptions.custom.badrequest;

public class InvalidIdException extends SimpleCustomBadRequestException {
    public InvalidIdException() {
        super("Invalid id");
    }

    public InvalidIdException(String message) {
        super(message);
    }
}
