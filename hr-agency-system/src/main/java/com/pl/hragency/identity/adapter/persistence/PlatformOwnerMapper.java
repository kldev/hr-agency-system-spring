package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.PlatformOwner;
import com.pl.hragency.identity.domain.model.PlatformOwnerId;
import com.pl.hragency.identity.domain.model.PlatformRole;
import org.springframework.stereotype.Component;

@Component
public class PlatformOwnerMapper {

    public PlatformOwner toDomain(PlatformOwnerJpaEntity entity) {
        return PlatformOwner.rehydrate(
                new PlatformOwnerId(entity.getId()),
                entity.getEmail(),
                PlatformRole.valueOf(entity.getRole()),
                entity.getPasswordHash(),
                entity.getCreatedAt()
        );
    }

    public PlatformOwnerJpaEntity toEntity(PlatformOwner owner) {
        return new PlatformOwnerJpaEntity(
                owner.id().value(),
                owner.email(),
                owner.role().name(),
                owner.createdAt(),
                owner.passwordHash()
        );
    }
}
