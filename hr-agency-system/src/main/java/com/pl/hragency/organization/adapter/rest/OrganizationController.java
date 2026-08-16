package com.pl.hragency.organization.adapter.rest;

import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.handler.CreateOrganizationHandler;
import com.pl.hragency.organization.application.result.CreateOrganizationResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/organization")
@Tag(name = "Organization")
@PreAuthorize("hasRole('OWNER')")
public class OrganizationController {
    private final CreateOrganizationHandler handler;

    public OrganizationController(CreateOrganizationHandler  handler) {
        this.handler = handler;

    }

    @PostMapping
    public CreateOrganizationResult createOrganization(@RequestBody CreateOrganizationCommand command) {

        return handler.handle(command);
    }
}
