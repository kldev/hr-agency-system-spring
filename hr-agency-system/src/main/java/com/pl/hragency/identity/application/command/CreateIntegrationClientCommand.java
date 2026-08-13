package com.pl.hragency.identity.application.command;

import com.pl.hragency.identity.domain.model.IntegrationScope;

import java.util.List;

public record CreateIntegrationClientCommand(String name, List<IntegrationScope> scopes) {

}
