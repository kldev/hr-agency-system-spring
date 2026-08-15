package com.pl.hragency.recruitment.domain.model.interview;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InterviewId(@NotNull UUID value) {

    public InterviewId {
        if (value == null) {
            throw new IllegalStateException("InterviewId is null");
        }
    }

    public static InterviewId newId() {
        return new InterviewId(UUID.randomUUID());
    }
}
