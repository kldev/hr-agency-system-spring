package com.pl.hragency.identity.application.command;

import jakarta.validation.constraints.NotBlank;

public record OwnerLoginCommand(@NotBlank String email, @NotBlank String password) {
}
