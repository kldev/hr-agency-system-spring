package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.PlatformOwner;

import java.util.Optional;

public interface PlatformOwnerRepository {
    Optional<PlatformOwner> findByEmail(String email);

    void save(
            PlatformOwner user);
}
