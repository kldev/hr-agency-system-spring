package com.pl.hragency.identity.domain.exception;

public class InvalidPasswordException  extends RuntimeException{

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(
            String message,
            Throwable cause) {

        super(message, cause);

    }
}
