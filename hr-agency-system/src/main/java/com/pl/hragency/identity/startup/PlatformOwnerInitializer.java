package com.pl.hragency.identity.startup;

import com.pl.hragency.identity.application.port.PasswordHasher;
import com.pl.hragency.identity.application.port.PlatformOwnerRepository;
import com.pl.hragency.identity.domain.model.PlatformOwner;
import com.pl.hragency.identity.domain.model.PlatformRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Component
public class PlatformOwnerInitializer {
    private final Logger logger = LoggerFactory.getLogger(PlatformOwnerInitializer.class);
    private final PlatformOwnerRepository repository;
    private final PasswordHasher hasher;
    private final String username;
    private final String password;

    public PlatformOwnerInitializer(@Value("${app.platform.user:owner:root}") String user,
                                    @Value("${app.platform.password:}") String password,
                                    PlatformOwnerRepository repository, PasswordHasher hasher) {
        this.repository = repository;
        this.username = user;
        this.password = password;
        this.hasher = hasher;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {

        if (repository.findByEmail(username).isPresent()) {
            return;
        }

        if (password.isBlank()) {
            logger.info(
                    "Platform owner setup skipped: PLATFORM_PASSWORD environment variable is not configured."
            );
            return;
        }


        var owner = PlatformOwner.create(username, PlatformRole.OWNER, hasher.hash(password));
        repository.save(owner);

        logger.info("Platform owner created successfully: {}", username);
        try {
            Files.writeString(
                    Path.of("platform-owner-password.txt"),
                    password,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            // ignore
        }
    }
}
