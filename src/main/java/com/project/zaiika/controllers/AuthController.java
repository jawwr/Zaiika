package com.project.zaiika.controllers;

import com.project.zaiika.models.auth.LoginCredential;
import com.project.zaiika.models.auth.RegisterCredential;
import com.project.zaiika.models.auth.WorkerCredential;
import com.project.zaiika.services.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCredential credential) {
        try {
            return new ResponseEntity<>(service.register(credential), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredential credential) {
        try {
            return ResponseEntity.ok(service.login(credential));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{placeId}/login")
    public ResponseEntity<?> login(@PathVariable("placeId") Long placeId, @RequestBody WorkerCredential credential) {
        try {
            return ResponseEntity.ok(service.login(placeId, credential));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
