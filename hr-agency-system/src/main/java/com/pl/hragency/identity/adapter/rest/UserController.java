package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.port.CurrentPrincipalProvider;
import com.pl.hragency.identity.application.handler.CreateUserHandler;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {

    private final CreateUserHandler handler;
    private final CurrentPrincipalProvider provider;

    public UserController(CreateUserHandler handler, CurrentPrincipalProvider provider) {
        this.handler = handler;
        this.provider = provider;
    }

    private ExecutionContext getContext() {
        var currentUser = provider.getRequiredUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping
    public UUID create(@RequestBody CreateUserCommand command){
        return handler.handle(getContext(), command).value();
    }
}
