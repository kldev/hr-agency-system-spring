package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateJobApplicationCommand(UUID jobPostingId,
                                          @NotBlank(message = "Email is required")
                                          @Size(max = 320, message = "Email must not exceed 320 characters")
                                          String email,
                                          String firstName,
                                          String lastName,
                                          String phone,
                                          @NotNull
                                          CandidateSource source) {
}
