package com.project.zaiika.controllers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.services.userServices.PlaceRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/role")
@Slf4j
public class ManagePlaceRoleController {
    private final PlaceRoleService service;

    @Autowired
    public ManagePlaceRoleController(PlaceRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaceRoles() {
        try {
            return ResponseEntity.ok(service.getAllRoles());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewRoles(@RequestBody PlaceRole role) {
        try {
            service.createRole(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") Long roleId) {
        try {
            service.deleteRole(roleId);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") Long roleId,
                                        @RequestBody PlaceRole role) {
        try {
            role.setId(roleId);
            service.updateRole(role);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
