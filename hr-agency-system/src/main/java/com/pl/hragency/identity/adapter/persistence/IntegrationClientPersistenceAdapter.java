package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.application.port.IntegrationClientRepository;
import com.pl.hragency.identity.domain.model.IntegrationClient;
import org.springframework.stereotype.Component;

import java.util.Optional;



@Component
public class IntegrationClientPersistenceAdapter implements IntegrationClientRepository {

    private final SpringDataIntegrationClientRepository repository;
    private final IntegrationClientMapper mapper;

    public IntegrationClientPersistenceAdapter(SpringDataIntegrationClientRepository repository, IntegrationClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(IntegrationClient integrationClient) {

        repository.save(mapper.toEntity(integrationClient));

    }

    @Override
    public Optional<IntegrationClient> findActiveByKeyId(String code) {
        return repository.findByKeyId(code).map(mapper::toDomain);

    }
}
