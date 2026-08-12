package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.domain.exception.InvalidIntegrationCredentialsException;
import com.pl.hragency.identity.domain.exception.InvalidLoginCommandException;
import com.pl.hragency.identity.domain.exception.InvalidPasswordException;
import com.pl.hragency.shared.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IdentityApiExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    ResponseEntity<ApiError> handleInvalidPasswordException(
            InvalidPasswordException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        ex.getMessage()));
    }

    @ExceptionHandler(InvalidLoginCommandException.class)
    ResponseEntity<ApiError> handleInvalidLoginCommandException(
            InvalidLoginCommandException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        ex.getMessage()));
    }
    //InvalidIntegrationCredentialsException

    @ExceptionHandler(InvalidIntegrationCredentialsException.class)
    ResponseEntity<ApiError> handleInvalidIntegrationCredentialsException(
            InvalidIntegrationCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        ex.getMessage()));
    }
}
