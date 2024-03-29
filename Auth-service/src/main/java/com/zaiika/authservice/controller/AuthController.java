package com.zaiika.authservice.controller;

import com.zaiika.authservice.model.authCredentials.LoginCredential;
import com.zaiika.authservice.model.authCredentials.RegisterCredential;
import com.zaiika.authservice.model.worker.WorkerCredential;
import com.zaiika.authservice.service.authService.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Регистрация пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    ref = "jwtResponse"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCredential credential) {
        return new ResponseEntity<>(service.register(credential), HttpStatus.CREATED);
    }

    @Operation(summary = "Логин пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    ref = "jwtResponse"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredential credential) {
        return ResponseEntity.ok(service.login(credential));
    }

    @Operation(summary = "Логин работника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    ref = "jwtResponse"
            )
    })
    @PostMapping("/{placeId}/login")
    public ResponseEntity<?> login(@PathVariable("placeId") Long placeId,
                                   @RequestBody WorkerCredential credential) {
        return ResponseEntity.ok(service.login(placeId, credential));
    }

    @GetMapping("/isValid")
    public ResponseEntity<?> isValidToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(service.isValidToken(token));
    }

    @GetMapping("/getUserId")
    public ResponseEntity<?> getUserId(@RequestParam("token") String token) {
        return ResponseEntity.ok(service.getUser(token).getId());
    }
}
