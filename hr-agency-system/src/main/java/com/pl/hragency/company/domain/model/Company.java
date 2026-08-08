package com.pl.hragency.company.domain.model;


import java.time.Instant;
import java.util.UUID;

public class Company {

    private final CompanyId id;
    private final CompanyOrganizationId organizationId;

    private final String name;
    private final TaxId taxId;
    private final Address address;
    private final RegistrationNumber registrationNumber;

    private final CompanyStatus status;

    private final UUID salesOwnerId;

    private final Instant createdAt;

    private Company(
            CompanyId id,
            CompanyOrganizationId organizationId,
            String name,
            TaxId taxId,
            Address address,
            RegistrationNumber registrationNumber,
            CompanyStatus status,
            UUID salesOwnerId,
            Instant createdAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.registrationNumber = registrationNumber;
        this.status = status;
        this.salesOwnerId = salesOwnerId;
        this.createdAt = createdAt;
    }

    public static Company create(
            CompanyOrganizationId organizationId,
            String name,
            TaxId taxId,
            Address address,
            RegistrationNumber registrationNumber,
            UUID salesOwnerId) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Company name cannot be empty");
        }

        return new Company(
                CompanyId.newId(),
                organizationId,
                name,
                taxId,
                address,
                registrationNumber,
                CompanyStatus.ACTIVE,
                salesOwnerId,
                Instant.now()
        );
    }

    public static Company rehydrate(
            CompanyId id,
            UUID organizationId,
            String name,
            String taxId,
            String countryCode,
            String city,
            String street,
            String postalCode,
            String registrationNumber,
            CompanyStatus status,
            UUID salesOwnerId,
            Instant createdAt) {

        return new Company(
                id,
                new CompanyOrganizationId(organizationId),
                name,
                new TaxId(taxId),
                new Address(new CountryCode(countryCode), city, street, postalCode),
                new RegistrationNumber(registrationNumber),
                status,
                salesOwnerId,
                createdAt
        );
    }

    public CompanyId id() {
        return id;
    }

    public CompanyOrganizationId organizationId() {
        return organizationId;
    }

    public String name() {
        return name;
    }

    public TaxId taxId() {
        return taxId;
    }

    public Address address() {
        return address;
    }

    public RegistrationNumber registrationNumber() {
        return registrationNumber;
    }

    public CompanyStatus status() {
        return status;
    }

    public UUID salesOwnerId() {
        return salesOwnerId;
    }

    public Instant createdAt() {
        return createdAt;
    }
}