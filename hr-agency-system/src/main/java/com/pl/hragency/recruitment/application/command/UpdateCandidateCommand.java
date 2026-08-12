package com.pl.hragency.recruitment.application.command;

public record UpdateCandidateCommand(String email,
                                     String firstName,
                                     String lastName,
                                     String phone) {
}
