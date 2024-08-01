package com.mapquizzes.exceptions.custom.unauthorized;

public class RefreshTokenException extends SimpleCustomUnauthorizedException {

    public RefreshTokenException() {
        super("Refresh token is not valid");
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
