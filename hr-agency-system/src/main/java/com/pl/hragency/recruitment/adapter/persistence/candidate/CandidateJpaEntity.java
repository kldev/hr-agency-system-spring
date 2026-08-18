package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "candidates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_candidates_organization_email",
                        columnNames = {
                                "organization_id",
                                "email_normalized"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_candidates_organization",
                        columnList = "organization_id"
                )
        }
)
public class CandidateJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(
            name = "email",
            nullable = false,
            length = 320
    )
    private String email;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            name = "phone",
            length = 50
    )
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private CandidateStatus status;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "source",
            nullable = false,
            length = 50
    )
    private CandidateSource source;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(
            name = "summary"
    )
    private String summary;

    @Version
    @Column(
            name = "version",
            nullable = false
    )
    private Long version;

    protected CandidateJpaEntity() {
    }

    /// Create new candidate constructor
    public CandidateJpaEntity(
            UUID id,
            UUID organizationId,
            String email,
            String firstName,
            String lastName,
            String phone,
            CandidateStatus status,
            CandidateSource source,
            Instant createdAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.status = status;
        this.source = source;
        this.createdAt = createdAt;
        this.version = null;
    }

    public void update(String email,
                       String firstName,
                       String lastName,
                       String phone,
                       CandidateStatus status,
                       String summary)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.status = status;
        this.summary = summary;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public CandidateStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public CandidateSource getSource() {
        return source;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getSummary() {
        return summary;
    }
}