package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.OwnerLoginCommand;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.application.service.PlatformLoginHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platform")
@Tag(name = "Owner")
public class PlatformOwnerController {

    private final PlatformLoginHandler platformLoginHandler;

    public PlatformOwnerController(PlatformLoginHandler platformLoginHandler) {
        this.platformLoginHandler = platformLoginHandler;
    }

    @PostMapping("login")
    public LoginResult login(@RequestBody OwnerLoginCommand command) {

        return platformLoginHandler.handle(command);
    }
}
