package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.IntegrationApiKeyHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptIntegrationApiKeyHasher implements IntegrationApiKeyHasher {

    private final PasswordEncoder passwordEncoder;

    public BCryptIntegrationApiKeyHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String secret) {
        return passwordEncoder.encode(secret);
    }

    @Override
    public boolean matches(String secret, String hash) {
        return passwordEncoder.matches(secret, hash);
    }
}