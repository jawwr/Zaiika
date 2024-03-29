package com.zaiika.workerservice.controller;

import com.zaiika.workerservice.model.PlaceRole;
import com.zaiika.workerservice.service.role.PlaceRoleService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
@Slf4j
@Tag(name = "Управление ролями в заведении")
public class PlaceRoleController {
    private final PlaceRoleService service;

    @Autowired
    public PlaceRoleController(PlaceRoleService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех ролей в заведении")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = PlaceRole.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasPermission(null, 'MANAGE_PLACE_ROLE')")
    @GetMapping
    public ResponseEntity<?> getAllPlaceRoles() {
        return ResponseEntity.ok(service.getAllRoles());
    }

    //TODO при создании роли можно задать привилегии
    @Operation(summary = "Создание новых ролей в заведении",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"name\" : \"some name\" }"
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
                                            implementation = PlaceRole.class
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasPermission(null, 'MANAGE_PLACE_ROLE')")
    @PostMapping
    public ResponseEntity<?> createNewRoles(@RequestBody PlaceRole role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(role));
    }

    @Operation(summary = "Удаление ролей в заведении")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
    @PreAuthorize("hasPermission(null, 'MANAGE_PLACE_ROLE')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") Long roleId) {
        service.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление роли в заведении по id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"name\" : \"some name\" }"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
    @PreAuthorize("hasPermission(null, 'MANAGE_PLACE_ROLE')")
    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") Long roleId,
                                        @RequestBody PlaceRole role) {
        role.setId(roleId);
        return ResponseEntity.ok(service.updateRole(role));
    }
}
