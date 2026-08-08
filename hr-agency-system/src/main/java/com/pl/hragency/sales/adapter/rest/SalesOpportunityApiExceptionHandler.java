package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.sales.domain.exception.CompanyNotFoundException;
import com.pl.hragency.shared.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SalesOpportunityApiExceptionHandler {

    @ExceptionHandler(CompanyNotFoundException.class)
    ResponseEntity<ApiError> handleCompanyNotFoundException(
            CompanyNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        ex.getMessage()));
    }
}
