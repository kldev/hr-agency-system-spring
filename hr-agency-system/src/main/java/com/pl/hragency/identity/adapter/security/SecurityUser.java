package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.domain.model.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record SecurityUser(@NotNull UUID userId, String email,
                           UUID orgId,
                           List<UserRole> roles,
                           String fullName) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map( m -> new SimpleGrantedAuthority("ROLE_" + m.toString())).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }
}

