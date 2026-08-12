package com.pl.hragency.recruitment.domain.exception;

import java.util.UUID;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(UUID candidateId) {
        super("Could not find candidate with id: " + candidateId);
    }
}
