package com.project.zaiika.controllers;

import com.project.zaiika.models.userModels.Role;
import com.project.zaiika.services.userServices.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-role")
@Slf4j
public class ManageRoleController {
    private final RoleService service;

    @Autowired
    public ManageRoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            return ResponseEntity.ok(service.getAllRoles());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable("roleId") Long roleId) {
        try {
            return ResponseEntity.ok(service.getRole(roleId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            service.createRole(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") Long roleId, @RequestBody Role role) {
        try {
            role.setId(roleId);
            service.updateRole(role);
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
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
