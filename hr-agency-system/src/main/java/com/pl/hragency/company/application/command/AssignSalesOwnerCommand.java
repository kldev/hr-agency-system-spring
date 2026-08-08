package com.pl.hragency.company.application.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignSalesOwnerCommand(@NotNull UUID salesUserId) {

}
