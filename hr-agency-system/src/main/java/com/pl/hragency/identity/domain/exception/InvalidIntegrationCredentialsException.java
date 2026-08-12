package com.pl.hragency.identity.domain.exception;

public class InvalidIntegrationCredentialsException extends RuntimeException {
    public InvalidIntegrationCredentialsException() {
        super("Invalid credential information");
    }
}
