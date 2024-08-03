package com.mapquizzes.exceptions.custom.badrequest;

public class EmptyFileException extends SimpleCustomBadRequestException {
    public EmptyFileException() {
        super("File is empty");
    }

    public EmptyFileException(String message) {
        super(message);
    }
}
