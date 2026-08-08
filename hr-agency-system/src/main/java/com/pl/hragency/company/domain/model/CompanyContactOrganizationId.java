package com.pl.hragency.company.domain.model;


import java.util.UUID;

public record CompanyContactOrganizationId(UUID value) {

    public CompanyContactOrganizationId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Organization id cannot be null"
            );
        }
    }
}
