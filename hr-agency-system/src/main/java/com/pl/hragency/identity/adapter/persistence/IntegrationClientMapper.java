package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.IntegrationClient;
import com.pl.hragency.identity.domain.model.IntegrationClientId;
import org.springframework.stereotype.Component;

@Component
public class IntegrationClientMapper {

    public IntegrationClient toDomain(IntegrationClientJpaEntity entity) {
        return IntegrationClient.rehydrate(
                new IntegrationClientId(entity.getId()),
                entity.getOrganizationId(),
                entity.getName(),
                entity.getKeyId(),
                entity.getApiKeyHash(),
                entity.getScopes(),
                entity.getRevokedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public IntegrationClientJpaEntity toEntity(IntegrationClient client) {
        return new IntegrationClientJpaEntity(
                client.id().value(),
                client.organizationId(),
                client.name(),
                client.keyId(),
                client.secretHash(),
                client.scopes(),
                client.revokedAt(),
                client.createdAt(),
                client.updatedAt()
        );
    }
}
