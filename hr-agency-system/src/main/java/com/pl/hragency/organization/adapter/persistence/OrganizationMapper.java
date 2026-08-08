package com.pl.hragency.organization.adapter.persistence;

import com.pl.hragency.organization.domain.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    public OrganizationJpaEntity toEntity(
            Organization organization) {

        return new OrganizationJpaEntity(
                organization.id().value(),
                organization.name(),
                organization.slug(),
                organization.createdAt()
        );
    }

    public Organization toDomain(
            OrganizationJpaEntity entity) {

        return Organization.rehydrate(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt()
        );
    }
}
