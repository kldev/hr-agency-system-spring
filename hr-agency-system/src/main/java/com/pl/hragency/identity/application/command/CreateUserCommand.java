package com.pl.hragency.identity.application.command;

import com.pl.hragency.identity.domain.model.UserRole;

public record CreateUserCommand(String username,
                                String password,
                                String firstName,
                                String lastName,
                                UserRole role) {
}

