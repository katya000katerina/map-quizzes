package com.mapquizzes.exceptions.custom;

public class NoCookiesReceivedException extends RuntimeException {
    public NoCookiesReceivedException() {
        super("No cookies received with request");
    }

    public NoCookiesReceivedException(String message) {
        super(message);
    }
}
