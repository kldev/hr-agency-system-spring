package com.pl.hragency.identity.application.command;

import com.pl.hragency.identity.domain.model.IntegrationScope;

import java.util.Set;

public record CreateIntegrationClientCommand(String name, Set<IntegrationScope> scopes) {

}
