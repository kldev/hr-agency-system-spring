package com.pl.hragency;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/")
public class RootController
{
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String Get(){
        return "HR Agency System";
    }
}
