package com.pl.hragency.identity.domain.exception;


public class InvalidLoginCommandException
        extends RuntimeException {
    public InvalidLoginCommandException(String message) {
        super(message);
    }

}
