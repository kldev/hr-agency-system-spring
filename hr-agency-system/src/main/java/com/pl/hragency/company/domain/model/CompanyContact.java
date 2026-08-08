package com.pl.hragency.company.domain.model;

import java.time.Instant;

public final class CompanyContact {

    private final CompanyContactId id;
    private final CompanyContactOrganizationId organizationId;
    private final CompanyContactCompanyId companyId;

    private ContactFirstName firstName;
    private ContactLastName lastName;
    private ContactEmail email;
    private ContactPhone phone;
    private JobTitle jobTitle;

    private boolean primaryContact;
    private final Instant createdAt;

    private CompanyContact(
            CompanyContactId id,
            CompanyContactOrganizationId organizationId,
            CompanyContactCompanyId companyId,
            ContactFirstName firstName,
            ContactLastName lastName,
            ContactEmail email,
            ContactPhone phone,
            JobTitle jobTitle,
            boolean primaryContact, Instant createdAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.jobTitle = jobTitle;
        this.primaryContact = primaryContact;
        this.createdAt = createdAt;
    }

    public static CompanyContact create(
            CompanyContactOrganizationId organizationId,
            CompanyContactCompanyId companyId,
            ContactFirstName firstName,
            ContactLastName lastName,
            ContactEmail email,
            ContactPhone phone,
            JobTitle jobTitle,
            boolean primaryContact) {

        return new CompanyContact(
                CompanyContactId.newId(),
                organizationId,
                companyId,
                firstName,
                lastName,
                email,
                phone,
                jobTitle,
                primaryContact, Instant.now()
        );
    }

    public static CompanyContact rehydrate(
            CompanyContactId id,
            CompanyContactOrganizationId organizationId,
            CompanyContactCompanyId companyId,
            ContactFirstName firstName,
            ContactLastName lastName,
            ContactEmail email,
            ContactPhone phone,
            JobTitle jobTitle,
            boolean primaryContact, Instant createdAt) {

        return new CompanyContact(
                id,
                organizationId,
                companyId,
                firstName,
                lastName,
                email,
                phone,
                jobTitle,
                primaryContact,
                createdAt
        );
    }

    public CompanyContactId id() {
        return id;
    }

    public CompanyContactOrganizationId organizationId() {
        return organizationId;
    }

    public CompanyContactCompanyId companyId() {
        return companyId;
    }

    public ContactFirstName firstName() {
        return firstName;
    }

    public ContactLastName lastName() {
        return lastName;
    }

    public ContactEmail email() {
        return email;
    }

    public ContactPhone phone() {
        return phone;
    }

    public JobTitle jobTitle() {
        return jobTitle;
    }

    public boolean primaryContact() {
        return primaryContact;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void changeContactData(
            String firstName,
            String lastName,
            String email,
            String phone,
            String jobTitle) {

        this.firstName = new ContactFirstName(firstName);
        this.lastName = new ContactLastName(lastName);
        this.email = new ContactEmail(email);
        this.phone = new ContactPhone(phone);
        this.jobTitle = new JobTitle(jobTitle);
    }

    public void makePrimary() {
        this.primaryContact = true;
    }

    public void removePrimary() {
        this.primaryContact = false;
    }
}
