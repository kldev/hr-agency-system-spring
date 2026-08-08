package com.pl.hragency.company.application.query;

import java.util.UUID;

public record CompanyListItem(
        UUID id,
        String name,
        String taxId,
        String registrationNumber,
        String countryCode,
        String city,
        String status,
        UUID salesOwnerId
) {
}
