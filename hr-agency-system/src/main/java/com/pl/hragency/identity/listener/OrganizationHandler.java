package com.pl.hragency.identity.listener;

import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.handler.CreateUserHandler;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.organization.domain.event.OrganizationCreateAdminEvent;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.core.annotation.Order;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationHandler {
    private final CreateUserHandler handler;
    private final UserRepository userRepository;

    public OrganizationHandler(CreateUserHandler handler, UserRepository userRepository) {
        this.handler = handler;
        this.userRepository = userRepository;
    }

    @ApplicationModuleListener
    @Order(2)
    void on(OrganizationCreateAdminEvent event) {

        var systemUser = userRepository.findByEmailAndOrganizationId("system", new UserOrganizationId(event.organizationId()))
                .orElse(null);

        var userId = systemUser == null ? UUID.randomUUID() : systemUser.id().value();

        var command = new CreateUserCommand(event.email(), event.password(), event.firstName(), event.lastName(), OrganizationRole.ADMIN);

        var context = new ExecutionContext(event.organizationId(), userId, "system");

        handler.handle(context, command);

    }
}
