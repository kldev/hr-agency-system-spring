package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.LoginCommand;
import com.pl.hragency.identity.application.port.LoginRateLimiter;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.application.handler.LoginHandler;
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
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final LoginHandler handler;
    private final LoginRateLimiter rateLimiter;

    public AuthController(LoginHandler handler, LoginRateLimiter limiter) {
        this.handler = handler;
        this.rateLimiter = limiter;
    }

    @SecurityRequirements({})
    @PostMapping("login")
    public LoginResult login(@Validated @RequestBody LoginCommand request,
                             HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();


        if (!rateLimiter.tryConsumeIp(ip)) {
            throw new TooManyLoginAttemptsException();
        }

        var accountAllowed = rateLimiter.tryConsumeAccount(
                request.orgSlug(),
                request.email()
        );

        if (!accountAllowed) {
            throw new TooManyLoginAttemptsException();
        }

        try {
            var result = handler.handle(request);

            rateLimiter.resetAccount(
                    request.orgSlug(),
                    request.email()
            );

            return result;

        } catch (InvalidLoginCommandException ex) {
            throw ex;
        }
    }
}
