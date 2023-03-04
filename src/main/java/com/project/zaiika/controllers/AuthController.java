package com.project.zaiika.controllers;

import com.project.zaiika.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {
    private final TokenService service;

    @Autowired
    public AuthController(TokenService service) {
        this.service = service;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        log.debug("Token requested for user: '{}'", authentication.getName());
        String token = service.generateToken(authentication);
        log.debug("Token granted {}", token);
        return token;
    }
}
