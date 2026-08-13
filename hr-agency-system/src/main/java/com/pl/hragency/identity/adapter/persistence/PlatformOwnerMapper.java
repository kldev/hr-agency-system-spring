package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.PlatformOwner;
import com.pl.hragency.identity.domain.model.PlatformOwnerId;
import com.pl.hragency.identity.domain.model.PlatformRole;
import org.springframework.stereotype.Component;

@Component
public class PlatformOwnerMapper {

    public PlatformOwner toDomain(PlatformUserJpaEntity entity) {
        return PlatformOwner.rehydrate(
                new PlatformOwnerId(entity.getId()),
                entity.getEmail(),
                PlatformRole.valueOf(entity.getRole()),
                entity.getPasswordHash(),
                entity.getCreatedAt()
        );
    }

    public PlatformUserJpaEntity toEntity(PlatformOwner owner) {
        return new PlatformUserJpaEntity(
                owner.id().value(),
                owner.email(),
                owner.role().name(),
                owner.createdAt(),
                owner.passwordHash()
        );
    }
}
