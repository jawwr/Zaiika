package com.project.zaiika.utils;

import com.project.zaiika.models.permission.AvailablePermission;
import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.repositories.permissions.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class PermissionUtils {
    private final PermissionRepository permissionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createPermissions() {
        List<Permission> permissions = new ArrayList<>();
        var availablePermission = AvailablePermission.values();
        for (int i = 1; i <= availablePermission.length; i++) {
            var name = availablePermission[i - 1].name();
            if (!permissionRepository.existsByName(name)) {
                var permission = new Permission(name);
                permissions.add(permission);
            }
        }

        if (permissions.size() > 0) {
            permissionRepository.saveAll(permissions);
        }
    }
}
