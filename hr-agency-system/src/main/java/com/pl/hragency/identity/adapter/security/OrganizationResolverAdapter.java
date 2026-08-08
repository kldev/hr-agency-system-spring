package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.OrganizationResolver;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.organization.api.OrganizationApi;
import org.springframework.stereotype.Service;

@Service
public class OrganizationResolverAdapter implements OrganizationResolver {

    private final OrganizationApi api;

    public OrganizationResolverAdapter(
            OrganizationApi api) {

        this.api = api;
    }

    @Override
    public UserOrganizationId resolve(String slug) {

        return new UserOrganizationId(api.findBySlug(slug).id());
    }

}
