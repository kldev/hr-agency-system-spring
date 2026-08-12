package com.pl.hragency.recruitment.domain.model.candidate;



import java.util.UUID;

public record CandidateId(UUID value) {
    public CandidateId {
        if (value == null) {
            throw new IllegalArgumentException("Candidate id cannot be null");
        }
    }

    public static CandidateId newId() {
        return new CandidateId(UUID.randomUUID());
    }
}
