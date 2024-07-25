package com.mapquizzes.exceptions;

import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.exceptions.custom.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiError> handleRefreshTokenException(RefreshTokenException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user credentials"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ":" +
                        Objects.requireNonNull(e.getDefaultMessage(), "Field validation error: " + e.getField()))
                .toList();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error", errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({EntityNotFoundException.class, NullIdException.class})
    public ResponseEntity<ApiError> handleInternalError(RuntimeException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
