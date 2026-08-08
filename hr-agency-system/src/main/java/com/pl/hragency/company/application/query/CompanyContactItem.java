package com.pl.hragency.company.application.query;

import java.util.UUID;

public record CompanyContactItem(UUID id,
                                 UUID companyId,
                                 String firstName,
                                 String lastName,
                                 String email,
                                 String phone,
                                 String jobTitle,
                                 boolean primaryContact) {
}
