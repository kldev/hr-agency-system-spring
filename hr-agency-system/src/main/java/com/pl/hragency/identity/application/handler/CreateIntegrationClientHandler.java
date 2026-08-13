package com.pl.hragency.identity.application.handler;

import com.pl.hragency.identity.application.command.CreateIntegrationClientCommand;
import com.pl.hragency.identity.application.port.IntegrationApiKeyHasher;
import com.pl.hragency.identity.application.port.IntegrationClientRepository;
import com.pl.hragency.identity.application.result.IntegrationClientResult;
import com.pl.hragency.identity.application.security.IntegrationKeyIdGenerator;
import com.pl.hragency.identity.domain.model.IntegrationClient;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateIntegrationClientHandler {
    private final IntegrationClientRepository repository;
    private final IntegrationApiKeyHasher apiKeyHasher;

    public CreateIntegrationClientHandler(IntegrationClientRepository repository, IntegrationApiKeyHasher apiKeyHasher) {
        this.repository = repository;
        this.apiKeyHasher = apiKeyHasher;
    }

    public IntegrationClientResult handle(ExecutionContext context, CreateIntegrationClientCommand command) {

        String secretValue =  IntegrationKeyIdGenerator.generate(20);

        IntegrationClient client = IntegrationClient.create(
                context.organizationId(),
                command.name(),
                "sk_" +  IntegrationKeyIdGenerator.generate(10),
                apiKeyHasher.hash(secretValue),
                command.scopes(), Instant.now());

        repository.save(client);

        return new IntegrationClientResult(client.keyId()+"."+secretValue);

    }
}
