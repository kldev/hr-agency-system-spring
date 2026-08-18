package com.pl.hragency.recruitment.adapter.web;

import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record JobApplicationForm(UUID jobPostingId,
                                 @NotBlank(message = "Email is required")
                                 @Size(max = 320, message = "Email must not exceed 320 characters")
                                 String email,

                                 @NotBlank( message = "First name is required")
                                 String firstName,

                                 @NotBlank( message = "Last name is required")
                                 String lastName,
                                 String phone) {

    public CreateJobApplicationCommand toCommand(
            UUID jobPostingId
    ) {
        return new CreateJobApplicationCommand(
                jobPostingId,
                firstName,
                lastName,
                email,
                phone,
                CandidateSource.CAREER_PAGE
        );
    }
}
