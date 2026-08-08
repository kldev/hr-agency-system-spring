package com.pl.hragency.jobdescription.domain.exception;

import java.util.UUID;

public class JobDescriptionNotFoundException
        extends RuntimeException {

    public JobDescriptionNotFoundException(
            UUID jobDescriptionId
    ) {
        super("Job description not found: " + jobDescriptionId);
    }
}
