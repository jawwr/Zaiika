package com.project.zaiika.controllers;

import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.services.userServices.PlaceRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/place/{placeId}/role")
@Slf4j
public class RoleController {
    private final PlaceRoleService service;

    @Autowired
    public RoleController(PlaceRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaceRoles(@PathVariable("placeId") Long placeId) {
        try {
            return ResponseEntity.ok(service.getAllRoles(placeId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewRoles(@PathVariable("placeId") Long placeId, @RequestBody PlaceRole role) {
        try {
            role.setPlaceId(placeId);
            service.createRole(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("placeId") Long placeId, @PathVariable("roleId") Long roleId) {
        try {
            service.deleteRole(placeId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("placeId") Long placeId,
                                        @PathVariable("roleId") Long roleId,
                                        @RequestBody PlaceRole role) {
        try {
            role.setId(roleId);
            role.setPlaceId(placeId);
            service.updateRole(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
