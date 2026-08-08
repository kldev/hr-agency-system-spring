package com.pl.hragency.identity.domain.model;

import java.util.Arrays;

public enum UserRole {

    ADMIN,
    RECRUITER,
    HIRING_MANAGER,
    INTERVIEWER,
    SALES;

    public static UserRole from(String value) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown user role: " + value
                        ));
    }
}
