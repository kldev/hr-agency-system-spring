package com.pl.hragency.testsupport;


import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.handler.CreateOrganizationHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestOrganizationFactory {

    private final CreateOrganizationHandler handler;

    public TestOrganizationFactory(
            CreateOrganizationHandler handler) {
        this.handler = handler;
    }

    public TestOrganization create() {

        var suffix = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        var slug = "test-" + suffix;

        var result = handler.handle(
                new CreateOrganizationCommand(
                        "Test Organization " + suffix,
                        slug
                )
        );

        return new TestOrganization(
                result.id(),
                slug
        );
    }
}