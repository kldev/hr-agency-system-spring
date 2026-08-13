package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;

import java.util.UUID;

public record CreateJobApplicationCommand(UUID jobPostingId,
                                          String email,
                                          String firstName,
                                          String lastName,
                                          String phone,
                                          CandidateSource source) {
}
