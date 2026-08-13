package com.pl.hragency.identity.application.command;

import com.pl.hragency.identity.domain.model.OrganizationRole;

public record CreateUserCommand(String username,
                                String password,
                                String firstName,
                                String lastName,
                                OrganizationRole role) {
}

