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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission")
@Tag(name = "Работа с привилегиями")
public class PermissionController {
    private final PermissionService service;

    @Autowired
    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех привилегий приложения")
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
    @PreAuthorize("hasAnyAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(service.getAllPermissions());
    }

    @Operation(summary = "Обновление привилегии по id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Permission.class
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_PERMISSION')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<?> updatePermission(@PathVariable("permissionId") long permissionId,
                                              @RequestBody Permission permission) {
        permission.setId(permissionId);
        service.updatePermission(permission);
        return ResponseEntity.ok().build();
    }
}
