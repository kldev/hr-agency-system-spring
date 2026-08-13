package com.pl.hragency.recruitment.adapter.rest;

import com.pl.hragency.recruitment.domain.exception.JobPostingNotActiveException;
import com.pl.hragency.shared.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JobPostingApiExceptionHandler {
    @ExceptionHandler(JobPostingNotActiveException.class)
    ResponseEntity<ApiError> handleJobPostingNotActiveException(
            JobPostingNotActiveException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(
                        ex.getMessage()));
    }
}
