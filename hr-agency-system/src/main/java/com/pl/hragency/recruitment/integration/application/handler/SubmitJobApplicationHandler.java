package com.pl.hragency.recruitment.integration.application.handler;

import com.pl.hragency.identity.api.CurrentIntegrationClient;
import com.pl.hragency.recruitment.integration.application.command.SubmitJobApplicationCommand;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubmitJobApplicationHandler {

    public UUID handle(CurrentIntegrationClient context, SubmitJobApplicationCommand command) {
        return UUID.randomUUID();
    }
}
