package com.mapquizzes.exceptions.custom;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
        super("Refresh token is not valid");
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
