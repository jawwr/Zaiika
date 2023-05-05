package com.zaiika.authservice.controller;

import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "Управление пользователями")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех пользователей приложения")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = User.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Operation(summary = "Добавление роли по id человека")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @PostMapping("/{userId}/role")
    public ResponseEntity<?> addRole(@PathVariable("userId") Long userId,
                                     @RequestParam("role") String role) {
        service.setRoleToUser(userId, role);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление роли по id человека")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @DeleteMapping("/{userId}/role")
    public ResponseEntity<?> deleteRole(@PathVariable("userId") Long userId,
                                        @RequestParam("role") String role) {
        service.deleteRoleFromUser(userId, role);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hasPermission")
    public ResponseEntity<?> hasPermission(@RequestParam("pName") String permissionName,
                                           @RequestHeader("AUTHORIZATION") String token) {
        return ResponseEntity.ok(service.hasUserPermission(token, permissionName.toUpperCase()));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("AUTHORIZATION") String token) {
        return ResponseEntity.ok(service.getUserInfo(token));
    }
}
