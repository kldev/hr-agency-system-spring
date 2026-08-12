package com.pl.hragency.recruitment.adapter.rest;

import com.pl.hragency.recruitment.domain.exception.JobPostingNotFoundException;
import com.pl.hragency.shared.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JobPostingApiExceptionHandler {

    @ExceptionHandler(JobPostingNotFoundException.class)
    ResponseEntity<ApiError> handleINotFoundException(
            JobPostingNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        ex.getMessage()));
    }
}
