package com.pl.hragency.company.application.command;

public record CreateCompanyContactCommand(String email, String phone, String firstName, String lastName, String jobTitle, boolean primaryContact)  {
}
