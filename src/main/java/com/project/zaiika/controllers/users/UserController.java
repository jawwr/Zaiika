package com.project.zaiika.controllers.users;

import com.project.zaiika.models.userModels.UserDto;
import com.project.zaiika.services.userServices.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-users")
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
                                                    implementation = UserDto.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(service.getAllUsers());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Добавление роли по id человека")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @PostMapping("/{userId}/role")
    public ResponseEntity<?> addRole(@PathVariable("userId") Long userId,
                                     @RequestParam("role") String role) {
        try {
            service.addUserRole(userId, role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Удаление роли по id человека")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @DeleteMapping("/{userId}/role")
    public ResponseEntity<?> deleteRole(@PathVariable("userId") Long userId,
                                        @RequestParam("role") String role) {
        try {
            service.deleteUserRole(userId, role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        try {
            service.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
