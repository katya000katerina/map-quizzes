package com.mapquizzes.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ApiError {
    private final OffsetDateTime timestampz;
    private HttpStatus httpStatus;
    private String message;
    private List<String> errors;

    private ApiError() {
        timestampz = OffsetDateTime.now();
    }

    public ApiError(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
        message = "";
        errors = new ArrayList<>();
    }

    public ApiError(HttpStatus httpStatus, String message) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        errors = new ArrayList<>();
    }

    public ApiError(HttpStatus httpStatus, String message, String error) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiError(HttpStatus httpStatus, String message, List<String> errors) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = errors;
    }
}
