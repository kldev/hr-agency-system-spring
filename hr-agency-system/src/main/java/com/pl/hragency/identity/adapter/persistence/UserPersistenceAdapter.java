package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import org.springframework.stereotype.Component;


import java.util.Optional;
import java.util.UUID;


@Component
public class UserPersistenceAdapter implements UserRepository {
    private final SpringDataUserRepository userRepository;
    private final UserMapper mapper;

    public UserPersistenceAdapter(SpringDataUserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByEmailAndOrganizationId(String email, UserOrganizationId organizationId) {
        return userRepository.findByEmailAndOrganizationId(email, organizationId.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public void save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        userRepository.save(entity);
    }

    @Override
    public boolean existsInOrganization(UUID userId, UUID organizationId) {
        return userRepository.existsByIdAndOrganizationId(userId, organizationId);
    }

    @Override
    public Optional<User> findUser(UUID userId, UUID organizationId) {
        return userRepository.findByIdAndOrganizationId(userId, organizationId).map(mapper::toDomain);
    }
}
