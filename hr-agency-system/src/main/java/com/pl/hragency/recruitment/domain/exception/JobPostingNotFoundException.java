package com.pl.hragency.recruitment.domain.exception;

public class JobPostingNotFoundException extends RuntimeException {
    public JobPostingNotFoundException() {
        super("Job posting not found");
    }
}
