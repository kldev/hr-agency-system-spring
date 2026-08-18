package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCandidateCommand(
        @Email
        @NotBlank
        String email, String firstName, String lastName, String phone,
        @NotNull
        CandidateSource source) {
}
