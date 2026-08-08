package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.company.domain.model.CompanyStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class CompanyJpaEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(
            name = "registration_number",
            nullable = false,
            length = 30
    )
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private CompanyStatus status;

    @Column(name = "sales_owner_id")
    private UUID salesOwnerId;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    protected CompanyJpaEntity() {
    }

    public CompanyJpaEntity(
            UUID id,
            UUID organizationId,
            String name,
            String countryCode,
            String taxId,
            String city,
            String address,
            String postalCode,
            String registrationNumber,
            CompanyStatus status,
            UUID salesOwnerId,
            Instant createdAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.countryCode = countryCode;
        this.taxId = taxId;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.registrationNumber = registrationNumber;
        this.status = status;
        this.salesOwnerId = salesOwnerId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public UUID getSalesOwnerId() {
        return salesOwnerId;
    }

    public void setSalesOwnerId(UUID salesOwnerId) {
        this.salesOwnerId = salesOwnerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
