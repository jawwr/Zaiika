package com.zaiika.workerservice.controller;

import com.zaiika.workerservice.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/permission")
@Slf4j
public class PermissionController {
    private final PermissionService service;

    @Autowired
    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @PreAuthorize("hasPermission(null, 'VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasPermission(null, 'VIEW_PERMISSIONS')")
    @GetMapping("/{permissionId}")
    public ResponseEntity<?> getPermissionById(@PathVariable("permissionId") long id) {
        return ResponseEntity.ok(service.getPermissionById(id));
    }
}
