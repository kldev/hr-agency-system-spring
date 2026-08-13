package com.pl.hragency.recruitment.application.command;

import java.util.UUID;

public record CreateJobApplicationCommand(UUID jobPostingId,
                                          String email,
                                          String firstName,
                                          String lastName,
                                          String phone) {
}
