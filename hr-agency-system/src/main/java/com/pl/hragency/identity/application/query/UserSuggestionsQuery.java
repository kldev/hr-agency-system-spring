package com.pl.hragency.identity.application.query;

import com.pl.hragency.identity.api.UserSuggestion;
import com.pl.hragency.identity.domain.model.OrganizationRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserSuggestionsQuery {
    List<UserSuggestion> find(
            UUID organizationId,
            String search,
            Set<OrganizationRole> roles
    );
}
