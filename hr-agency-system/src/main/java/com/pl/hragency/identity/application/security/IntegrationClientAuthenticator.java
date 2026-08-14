package com.pl.hragency.identity.application.security;

import com.pl.hragency.constants.SystemAccountNames;
import com.pl.hragency.identity.application.port.IntegrationApiKeyHasher;
import com.pl.hragency.identity.application.port.IntegrationClientRepository;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.exception.InvalidIntegrationCredentialsException;
import com.pl.hragency.identity.domain.model.IntegrationClient;

import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IntegrationClientAuthenticator {

    private final IntegrationClientRepository repository;
    private final IntegrationApiKeyHasher apiKeyHasher;
    private final UserRepository userRepository;

    public IntegrationClientAuthenticator(
            IntegrationClientRepository repository,
            IntegrationApiKeyHasher apiKeyHasher, UserRepository userRepository) {
        this.repository = repository;
        this.apiKeyHasher = apiKeyHasher;
        this.userRepository = userRepository;
    }

    public IntegrationAuthentication authenticate(String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new InvalidIntegrationCredentialsException();
        }

        String keyId = extractKeyId(apiKey);
        String secret = extractSecret(apiKey);

        IntegrationClient client = repository
                .findActiveByKeyId(keyId)
                .orElseThrow(InvalidIntegrationCredentialsException::new);

        if (!apiKeyHasher.matches(secret, client.secretHash())) {
            throw new InvalidIntegrationCredentialsException();
        }

        User user = userRepository.findByEmailAndOrganizationId(SystemAccountNames.INTEGRATIONS, new UserOrganizationId(client.organizationId()))
                .orElseThrow(InvalidIntegrationCredentialsException::new);

        return new IntegrationAuthentication(
                client.id().value(),
                client.organizationId(),
                client.name(),
                client.scopes(),
                user.id().value()
        );
    }

    private String extractKeyId(String apiKey) {
        // np. hrk_live_xxxxx.secret
        int separator = apiKey.indexOf('.');

        if (separator <= 0) {
            throw new InvalidIntegrationCredentialsException();
        }

        return apiKey.substring(0, separator);
    }

    private String extractSecret(String apiKey) {
        int separator = apiKey.indexOf('.');

        if (separator <= 0 || separator == apiKey.length() - 1) {
            throw new InvalidIntegrationCredentialsException();
        }

        return apiKey.substring(separator + 1);
    }
}