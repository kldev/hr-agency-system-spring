package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.User;

public interface TokenGenerator {
    String generate(User user);
}
