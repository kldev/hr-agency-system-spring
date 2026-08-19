package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@org.hibernate.annotations.Immutable
@Table(name = "job_application_view")
public class JobApplicationReadJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "candidate_id")
    private UUID candidateId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "recruiter_id")
    private UUID recruiterId;

    @Column(name = "job_posting_id")
    private UUID jobPostingId;

    @Column(name = "candidate_first_name")
    private String candidateFirstName;

    @Column(name = "candidate_last_name")
    private String candidateLastName;

    @Column(name = "candidate_email")
    private String candidateEmail;

    @Column(name = "candidate_phone")
    private String candidatePhone;

    @Column(name = "recruiter_fullname")
    private String recruiterFullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobApplicationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private CandidateSource source;

    @Column(name = "created_at")
    private Instant createdAt;

    protected JobApplicationReadJpaEntity() {}

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public UUID getRecruiterId() {
        return recruiterId;
    }

    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public String getRecruiterFullName() {
        return recruiterFullName;
    }

    public String getCandidatePhone() {
        return candidatePhone;
    }

    public CandidateSource getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getJobPostingId() {
        return jobPostingId;
    }
}
