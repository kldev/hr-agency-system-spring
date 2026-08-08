package com.pl.hragency.testsupport;

import com.pl.hragency.identity.application.command.LoginCommand;
import com.pl.hragency.identity.application.result.LoginResult;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.client.RestTestClient;

@Component
public class AuthenticationTestClient {

    private final RestTestClient client;

    public AuthenticationTestClient(
            RestTestClient client) {
        this.client = client;
    }

    public String login(TestUser user) {

        return client
                .post()
                .uri("/api/auth/login")
                .body(new LoginCommand(
                        user.email(),
                        user.password(),
                        user.organizationSlug()
                ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LoginResult.class)
                .returnResult()
                .getResponseBody()
                .token();
    }
}
