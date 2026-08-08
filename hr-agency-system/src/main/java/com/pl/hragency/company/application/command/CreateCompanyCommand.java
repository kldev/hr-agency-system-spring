package com.pl.hragency.company.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCompanyCommand(
        @NotBlank
        String name,

        @NotBlank
        @Size(min = 2, max = 2)
        String countryCode,

        String taxNumber,

        String registrationNumber,
        String city,
        String street,
        String postalCode
) {
}
