package com.pl.hragency.shared.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiValidationError> handleValidationException(
            MethodArgumentNotValidException ex) {

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiValidationError.ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest()
                .body(new ApiValidationError(
                        "Request validation failed",
                        errors
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ApiError> handleEntityNotFound(
            EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiError("Invalid request parameter"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiError("Invalid request body"));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleUnexpectedException(
            Exception ex) {

        log.error("Unexpected error", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Internal server error"));
    }
}