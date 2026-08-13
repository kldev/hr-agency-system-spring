package com.pl.hragency.identity.application.query;


import com.pl.hragency.identity.adapter.persistence.UserQueryRepository;
import com.pl.hragency.identity.adapter.persistence.UserSpecifications;
import com.pl.hragency.identity.adapter.persistence.UserSuggestionProjection;
import com.pl.hragency.identity.api.UserSuggestion;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserSuggestionsQueryService
        implements UserSuggestionsQuery {

    private static final int LIMIT = 25;

    private final UserQueryRepository repository;

    public UserSuggestionsQueryService(
            UserQueryRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public List<UserSuggestion> find(
            UUID organizationId,
            String search,
            Set<OrganizationRole> roles
    ) {
        var pageable = PageRequest.of(0, LIMIT,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return repository
                .suggestions(
                        Specification.allOf(UserSpecifications.organizationId(organizationId),
                                UserSpecifications.search(search),
                                UserSpecifications.roles(roles)),
                        pageable
                )
                .stream()
                .map(this::toOutput).toList();


    }

    UserSuggestion toOutput(UserSuggestionProjection input) {
        return new UserSuggestion(input.id(), input.firstName() + " " + input.lastName(), input.email());
    }
}