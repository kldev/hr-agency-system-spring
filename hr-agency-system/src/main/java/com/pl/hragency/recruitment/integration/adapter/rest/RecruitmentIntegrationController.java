package com.pl.hragency.recruitment.integration.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.integration.application.command.SubmitJobApplicationCommand;
import com.pl.hragency.recruitment.integration.application.handler.SubmitJobApplicationHandler;
import com.pl.hragency.shared.rest.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/integrations/job-applications")
@Tag(name = "Integrations")
public class RecruitmentIntegrationController {

    private final Logger logger = LoggerFactory.getLogger(RecruitmentIntegrationController.class);
    private final IdentityApi identityApi;
    private final SubmitJobApplicationHandler handler;

    public RecruitmentIntegrationController(IdentityApi identityApi,
                                            SubmitJobApplicationHandler handler) {
        this.identityApi = identityApi;
        this.handler = handler;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_APPLICATION_WRITE')")
    public ApiResult submitJobApplication(Principal principal, @Validated @RequestBody SubmitJobApplicationCommand command)
    {
        var client = identityApi.gCurrentIntegrationClient();

        logger.info("submitting job application {} {} - client {}",
                command.jobPostingId(), command.email(),
                client.clientName()
        );

        handler.handle(client, command);

        return new ApiResult("Application submitted", true);
    }
}
