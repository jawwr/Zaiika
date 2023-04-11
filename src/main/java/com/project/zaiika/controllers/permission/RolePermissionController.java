package com.project.zaiika.controllers.permission;

import com.project.zaiika.services.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role/{roleId}/permissions")
@Slf4j
public class RolePermissionController {
    private final PermissionService service;

    @Autowired
    public RolePermissionController(PermissionService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<?> getPermissions(@PathVariable("roleId") long roleId) {
        return ResponseEntity.ok(service.getRolePermissions(roleId));
    }

    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE_ROLE')")
    @PutMapping
    public ResponseEntity<?> setPermissions(@PathVariable("roleId") long roleId,
                                            @RequestBody List<String> permissions) {
        service.setPermissions(roleId, permissions);
        return ResponseEntity.ok().build();
    }
}
