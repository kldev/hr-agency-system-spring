package com.pl.hragency.identity.application.handler;


import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.port.PasswordHasher;
import com.pl.hragency.identity.application.port.UserRepository;

import com.pl.hragency.identity.domain.event.UserCreatedEvent;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserId;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher hasher;
    private final EventPublisher eventPublisher;

    public CreateUserHandler(UserRepository userRepository,
                             PasswordHasher hasher,
                             EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserId handle(ExecutionContext context, CreateUserCommand command) {

        UserOrganizationId organizationId = new UserOrganizationId(
                context.organizationId());

        User user = User.create(
                organizationId,
                command.username(),
                command.firstName(),
                command.lastName(),
                command.role(),
                hasher.hash(command.password())
        );

        userRepository.save(user);

        var event = new UserCreatedEvent(
                user.id().value(),
                organizationId.value(),
                command.firstName() + " " +command.lastName(),
                command.username(),
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);

        return user.id();
    }
}
