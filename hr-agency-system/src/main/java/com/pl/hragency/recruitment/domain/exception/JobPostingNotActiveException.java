package com.pl.hragency.recruitment.domain.exception;

public class JobPostingNotActiveException extends RuntimeException{
    public JobPostingNotActiveException() {
        super("Job posting not active.");
    }
}
