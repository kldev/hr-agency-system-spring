package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;

public record CreateCandidateCommand(String email, String firstName, String lastName, String phone, CandidateSource source) {
}
