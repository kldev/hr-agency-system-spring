package com.pl.hragency.identity.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserOrganizationId (@NotNull UUID value){
    public UserOrganizationId {
        if (value == null) throw new IllegalArgumentException("value cannot be null");
    }

    public static UserOrganizationId newId(){
        return new UserOrganizationId(UUID.randomUUID());
    }
}
