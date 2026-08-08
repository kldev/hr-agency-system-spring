package com.pl.hragency.company.domain.model;

import java.util.UUID;

public record CompanyContactCompanyId(UUID value) {

    public CompanyContactCompanyId {
        if (value == null) {
            throw new IllegalArgumentException("Company id cannot be null");
        }
    }
}
