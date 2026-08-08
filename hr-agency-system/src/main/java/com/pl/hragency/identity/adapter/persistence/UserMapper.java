package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserId;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return User.rehydrate(
                new UserId(entity.getId()),
                new UserOrganizationId(entity.getOrganizationId()),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole(),
                entity.getPasswordHash(),
                entity.getCreatedAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.id().value(),
                user.organizationId().value(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.role(),
                user.passwordHash(),
                user.createdAt()
        );
    }
}
