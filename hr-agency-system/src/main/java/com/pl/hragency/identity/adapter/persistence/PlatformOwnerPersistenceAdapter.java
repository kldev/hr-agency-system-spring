package com.pl.hragency.identity.adapter.persistence;
import com.pl.hragency.identity.application.port.PlatformOwnerRepository;
import com.pl.hragency.identity.domain.model.PlatformOwner;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Component
public class PlatformOwnerPersistenceAdapter implements PlatformOwnerRepository {
    private final SpringDataPlatformUserRepository repository;
    private final PlatformOwnerMapper mapper;

    public PlatformOwnerPersistenceAdapter(SpringDataPlatformUserRepository repository, PlatformOwnerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PlatformOwner> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public void save(PlatformOwner user) {
        repository.save(mapper.toEntity(user));;
    }
}
