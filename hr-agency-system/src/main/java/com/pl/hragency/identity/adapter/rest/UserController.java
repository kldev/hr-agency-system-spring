package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.service.CreateUserHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {

    private final CreateUserHandler handler;

    public UserController(CreateUserHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserCommand command){
        return ResponseEntity.ok(handler.handle(command));
    }
}
