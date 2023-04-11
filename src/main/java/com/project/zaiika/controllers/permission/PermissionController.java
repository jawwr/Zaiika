package com.project.zaiika.controllers.permission;

import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.services.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    private final PermissionService service;

    @Autowired
    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(service.getAllPermissions());
    }

    @PreAuthorize("hasAnyAuthority('MANAGE_PERMISSION')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<?> updatePermission(@PathVariable("permissionId") long permissionId,
                                              @RequestBody Permission permission) {
        permission.setId(permissionId);
        service.updatePermission(permission);
        return ResponseEntity.ok().build();
    }
}
