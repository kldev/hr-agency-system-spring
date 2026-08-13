package com.pl.hragency.testsupport;

import com.pl.hragency.identity.domain.model.PlatformRole;

import java.util.UUID;

public record TestOwner(UUID userId, String email, String password, PlatformRole role) {
}
