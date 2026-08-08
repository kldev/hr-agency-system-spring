package com.pl.hragency.organization.application.command;

public record CreateOrganizationCommand(
        String name,
        String slug
) {
}
