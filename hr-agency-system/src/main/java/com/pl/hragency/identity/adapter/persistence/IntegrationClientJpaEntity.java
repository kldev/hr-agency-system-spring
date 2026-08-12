package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.IntegrationScope;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "integration_clients",
        indexes = {
                @Index(
                        name = "idx_integration_clients_organization",
                        columnList = "organization_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_integration_clients_key_id",
                        columnNames = "key_id"
                )
        }
)
public class IntegrationClientJpaEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(
            name = "organization_id",
            nullable = false,
            updatable = false
    )
    private UUID organizationId;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            name = "key_id",
            nullable = false,
            updatable = false,
            length = 100
    )
    private String keyId;

    @Column(
            name = "api_key_hash",
            nullable = false,
            updatable = false,
            length = 255
    )
    private String apiKeyHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "integration_client_scopes",
            joinColumns = @JoinColumn(
                    name = "integration_client_id",
                    referencedColumnName = "id"
            )
    )
    @Column(
            name = "scope",
            nullable = false,
            length = 50
    )
    @Enumerated(EnumType.STRING)
    private Set<IntegrationScope> scopes = new HashSet<>();

    @Column(
            name = "revoked_at",
            nullable = false
    )
    private Instant revokedAt;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;

    protected IntegrationClientJpaEntity() {
    }

    public IntegrationClientJpaEntity(
            UUID id,
            UUID organizationId,
            String name,
            String keyId,
            String apiKeyHash,
            Set<IntegrationScope> scopes,
            Instant revokedAt,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.keyId = keyId;
        this.apiKeyHash = apiKeyHash;
        this.scopes = new HashSet<>(scopes);
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getApiKeyHash() {
        return apiKeyHash;
    }

    public Set<IntegrationScope> getScopes() {
        return Set.copyOf(scopes);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String name,
            Set<IntegrationScope> scopes,
            Instant updatedAt) {

        this.name = name;
        this.scopes.clear();
        this.scopes.addAll(scopes);
        this.updatedAt = updatedAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }
}

