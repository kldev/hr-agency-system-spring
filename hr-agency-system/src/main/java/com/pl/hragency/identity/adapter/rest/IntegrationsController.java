package com.pl.hragency.identity.adapter.rest;


import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.application.command.CreateIntegrationClientCommand;
import com.pl.hragency.identity.application.result.IntegrationClientResult;
import com.pl.hragency.identity.application.handler.CreateIntegrationClientHandler;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration-clients")
@Tag(name = "Integrations Clients")
public class IntegrationsController {
    private final IdentityApi identityApi;
    private final CreateIntegrationClientHandler handler;

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    public IntegrationsController(IdentityApi identityApi, CreateIntegrationClientHandler handler) {
        this.identityApi = identityApi;
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public IntegrationClientResult create(@RequestBody CreateIntegrationClientCommand command) {
        return handler.handle(getContext(), command);
    }
}
