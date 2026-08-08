package com.pl.hragency.sales.domain.exception;

public class CompanyNotFoundException  extends RuntimeException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
