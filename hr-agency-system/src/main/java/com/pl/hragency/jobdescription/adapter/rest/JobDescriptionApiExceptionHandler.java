package com.pl.hragency.jobdescription.adapter.rest;

import com.pl.hragency.jobdescription.domain.exception.JobDescriptionNotFoundException;
import com.pl.hragency.shared.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JobDescriptionApiExceptionHandler {

    @ExceptionHandler(JobDescriptionNotFoundException.class)
    ResponseEntity<ApiError> handleINotFoundException(
            JobDescriptionNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        ex.getMessage()));
    }
}
