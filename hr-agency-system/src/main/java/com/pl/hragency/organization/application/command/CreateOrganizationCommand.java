package com.pl.hragency.organization.application.command;

public record CreateOrganizationCommand(
        String name,
        String slug,
        OrganizationAdmin organizationAdmin
) {
    public record OrganizationAdmin(String email, String password, String firstName, String latName) {}
}
