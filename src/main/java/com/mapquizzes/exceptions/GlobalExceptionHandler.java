package com.mapquizzes.exceptions;

import com.mapquizzes.exceptions.custom.badrequest.SimpleCustomBadRequestException;
import com.mapquizzes.exceptions.custom.internalservererror.SimpleCustomInternalServerException;
import com.mapquizzes.exceptions.custom.unauthorized.SimpleCustomUnauthorizedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        logger.error(ex);
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user credentials"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error(ex);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ":" +
                        Objects.requireNonNull(e.getDefaultMessage(), "Field validation error: " + e.getField()))
                .toList();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error", errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(SimpleCustomUnauthorizedException.class)
    public ResponseEntity<ApiError> handleRefreshTokenException(SimpleCustomUnauthorizedException ex) {
        logger.error(ex);
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(SimpleCustomBadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(SimpleCustomBadRequestException ex) {
        logger.error(ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(SimpleCustomInternalServerException.class)
    public ResponseEntity<ApiError> handleInternalServerException(SimpleCustomInternalServerException ex) {
        logger.error(ex);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
