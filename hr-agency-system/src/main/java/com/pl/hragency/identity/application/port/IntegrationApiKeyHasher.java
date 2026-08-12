package com.pl.hragency.identity.application.port;

public interface IntegrationApiKeyHasher {

    String hash(String secret);

    boolean matches(String secret, String hash);
}
