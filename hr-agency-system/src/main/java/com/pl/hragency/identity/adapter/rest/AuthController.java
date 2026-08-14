package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.LoginCommand;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.application.handler.LoginHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final LoginHandler handler;

    public AuthController(LoginHandler handler) {
        this.handler = handler;
    }

    @SecurityRequirements({})
    @PostMapping("login")
    public LoginResult login(@RequestBody LoginCommand request) {

        return handler.handle(request);
    }
}
