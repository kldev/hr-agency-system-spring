package com.pl.hragency.identity.application.service;


import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.port.CurrentUserProvider;
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

@Service
@Transactional
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final CurrentUserProvider userProvider;
    private final PasswordHasher hasher;
    private final EventPublisher eventPublisher;

    public CreateUserHandler(UserRepository userRepository, CurrentUserProvider userProvider, PasswordHasher hasher, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userProvider = userProvider;
        this.hasher = hasher;
        this.eventPublisher = eventPublisher;
    }

    public UserId handle(CreateUserCommand command) {
        /// TODO: validation and errors
        UserOrganizationId organizationId = new UserOrganizationId(
                userProvider.get().organizationId());
        User user = User.create(
                organizationId,
                command.username(),
                command.firstName(),
                command.lastName(),
                command.role(),
                hasher.hash(command.password())
        );

        userRepository.save(user);

        eventPublisher.publish(new UserCreatedEvent(user.id().value(),
                organizationId.value(),
                command.firstName() + " " +command.lastName(),
                command.username()));

        return user.id();
    }
}
