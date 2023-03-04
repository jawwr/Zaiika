package com.project.zaiika.controllers;

import com.project.zaiika.models.userModels.UserCredential;
import com.project.zaiika.services.jwtServices.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {
    private final TokenService service;
    private final AuthenticationManager manager;

    @Autowired
    public AuthController(TokenService service, AuthenticationManager manager) {
        this.service = service;
        this.manager = manager;
    }

    @PostMapping("/token")
    public String token(@RequestBody UserCredential credential) {
        var auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.getLogin(),
                        credential.getPassword()
                )
        );
        return service.generateToken(auth);
    }
}
