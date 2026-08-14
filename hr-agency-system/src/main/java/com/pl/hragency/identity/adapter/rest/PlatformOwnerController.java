package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.OwnerLoginCommand;
import com.pl.hragency.identity.application.port.LoginRateLimiter;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.application.handler.PlatformLoginHandler;
import com.pl.hragency.identity.domain.exception.InvalidLoginCommandException;
import com.pl.hragency.identity.domain.exception.TooManyLoginAttemptsException;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platform")
@Tag(name = "Owner")
public class PlatformOwnerController {

    private final PlatformLoginHandler platformLoginHandler;
    private final LoginRateLimiter rateLimiter;

    public PlatformOwnerController(PlatformLoginHandler platformLoginHandler, LoginRateLimiter rateLimiter) {
        this.platformLoginHandler = platformLoginHandler;
        this.rateLimiter = rateLimiter;
    }

    @SecurityRequirements({})
    @PostMapping("login")
    public LoginResult login(@Validated @RequestBody OwnerLoginCommand command,
                             HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();

        if (!rateLimiter.tryConsumeIp(ip)) {
            throw new TooManyLoginAttemptsException();
        }

        var accountAllowed = rateLimiter.tryConsumeAccount(
                "platform-hr-agency",
                command.email()
        );

        if (!accountAllowed) {
            throw new TooManyLoginAttemptsException();
        }

        try {
            var result = platformLoginHandler.handle(command);

            rateLimiter.resetAccount(
                    "platform-hr-agency",
                    command.email()
            );

            return result;

        } catch (InvalidLoginCommandException ex) {
            throw ex;
        }
    }
}
