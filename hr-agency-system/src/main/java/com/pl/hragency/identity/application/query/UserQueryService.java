package com.pl.hragency.identity.application.query;

import com.pl.hragency.identity.adapter.persistence.SpringDataUserRepository;
import com.pl.hragency.identity.adapter.persistence.UserJpaEntity;
import com.pl.hragency.identity.application.port.CurrentPrincipalProvider;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final SpringDataUserRepository repository;
    private final CurrentPrincipalProvider currentUserProvider;

    public UserQueryService(
            SpringDataUserRepository repository,
            CurrentPrincipalProvider currentUserProvider) {

        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
    }

    public Page<UserListItem> findAll(UserListQuery query) {

        var organizationId =
                currentUserProvider
                        .getRequiredUser()
                        .organizationId();


        Page<UserJpaEntity> users;

        if (query.search() == null
                || query.search().isBlank()) {

            users = repository.findAllByOrganizationId(
                    organizationId, query.pageable());

        } else {

            users = repository
                    .findAllByOrganizationIdAndEmailContainingIgnoreCase(
                            organizationId,
                            query.search(),
                            query.pageable());
        }

        return users.map(this::toItem);

    }

    private UserListItem toItem(
            UserJpaEntity user) {

        return new UserListItem(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}