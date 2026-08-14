package com.pl.hragency.shared.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    // NullPointerException
    @ExceptionHandler(
            NullPointerException.class
    )
    ResponseEntity<?> handleNullError(
            NullPointerException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiError(ex.getMessage())
                );

    }

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    ResponseEntity<?> handleIllegalArgumentError(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                       new ApiError(ex.getMessage())
                );

    }

    @ExceptionHandler(
            IllegalStateException.class
    )
    ResponseEntity<?> handleIllegalArgumentError(
            IllegalStateException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiError(ex.getMessage())
                );

    }

    //EntityNotFoundException
    @ExceptionHandler(
            EntityNotFoundException.class
    )
    ResponseEntity<?> handleEntityNotFoundError(
            EntityNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ApiError(ex.getMessage())
                );

    }


}
