package com.pl.hragency.identity.application.query;

import com.pl.hragency.identity.adapter.persistence.SpringDataUserRepository;
import com.pl.hragency.identity.adapter.persistence.UserJpaEntity;
import com.pl.hragency.identity.adapter.persistence.UserSpecifications;
import com.pl.hragency.identity.application.port.CurrentPrincipalProvider;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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

       var specifications = Specification.allOf(
               UserSpecifications.organizationId(organizationId),
               UserSpecifications.search(query.search())
       );

        var users = repository.findAll(specifications,query.pageable());

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