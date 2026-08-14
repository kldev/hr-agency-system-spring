package com.pl.hragency.identity.domain.model;

import java.util.Arrays;

public enum OrganizationRole {

    ADMIN,
    RECRUITER,
    HIRING_MANAGER,
    INTERVIEWER,
    SALES,
    SYSTEM;

    public static OrganizationRole from(String value) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown user role: " + value
                        ));
    }
}
