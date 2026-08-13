package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.PlatformOwner;

import java.util.Optional;

public interface PlatformUserRepository {
    Optional<PlatformOwner> findByEmail(String email);

    void save(
            PlatformOwner user);
}
