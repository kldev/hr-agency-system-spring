package com.pl.hragency.identity.domain.exception;

public class TooManyLoginAttemptsException
        extends RuntimeException {

    public TooManyLoginAttemptsException() {
        super("Too many login attempts");
    }
}
