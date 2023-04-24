package com.project.zaiika.controllers.permission;

import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.services.permission.PermissionService;
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

import java.util.List;

@RestController
@RequestMapping("/api/role/{roleId}/permissions")
@Slf4j
@Tag(name = "Работа с привилегиями")
public class RolePermissionController {
    private final PermissionService service;

    @Autowired
    public RolePermissionController(PermissionService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех привилегий для роли")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Permission.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE_ROLE')")
    @GetMapping
    public ResponseEntity<?> getPermissions(@PathVariable("roleId") long roleId) {
        return ResponseEntity.ok(service.getRolePermissions(roleId));
    }

    @Operation(summary = "Обновление привилегий",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE_ROLE')")
    @PutMapping
    public ResponseEntity<?> setPermissions(@PathVariable("roleId") long roleId,
                                            @RequestBody List<String> permissions) {
        service.setPermissions(roleId, permissions);
        return ResponseEntity.ok().build();
    }
}
