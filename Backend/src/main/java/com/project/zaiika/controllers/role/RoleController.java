package com.project.zaiika.controllers.role;

import com.project.zaiika.models.roles.Role;
import com.project.zaiika.services.roleServices.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/api/manage-role")
@Slf4j
@Tag(name = "Управление ролями приложения")
public class RoleController {
    private final RoleService service;

    @Autowired
    public RoleController(RoleService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех ролей в приложении")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Role.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE')")
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(service.getAllRoles());
    }

    @Operation(summary = "Получение роли в приложении по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Role.class
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE')")
    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable("roleId") Long roleId) {
        return ResponseEntity.ok(service.getRole(roleId));
    }

    @Operation(summary = "Создание роли в приложении",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"name\" : \"string\" }"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Role.class
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE')")
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(service.createRole(role));
    }

    @Operation(summary = "Обновление роли в приложении",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"name\" : \"string\" }"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE')")
    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") Long roleId,
                                        @RequestBody Role role) {
        role.setId(roleId);
        service.updateRole(role);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление роли в приложении по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_ROLE')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") Long roleId) {
        service.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }
}
