package com.pl.hragency.company.api;

import java.util.List;
import java.util.UUID;

public interface CompanyApi {
    UUID create(UUID userId,
                UUID organizationId, String name, String countryCode,
                String taxId, String registrationNumber,
                String city, String address, String postalCode);

    void createContact(UUID userId,
                       UUID  organizationId,
                       UUID companyId,
                       String firstName,
                       String lastName,
                       String phone,
                       String email,
                       String jobTitle);

    List<UUID> findAllIds(UUID organizationId, int pageSize);
    boolean exists(UUID organizationId, UUID companyId);
    List<CompanySuggestion> findCompanySuggestions(UUID organizationId, String search, String countryCode);
}
