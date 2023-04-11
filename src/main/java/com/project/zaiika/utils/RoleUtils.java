package com.project.zaiika.utils;

import com.project.zaiika.models.permission.AvailablePermission;
import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.models.roles.Role;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.repositories.permissions.PermissionRepository;
import com.project.zaiika.repositories.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleUtils {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        List<Role> roles = new ArrayList<>();
        var availableRoles = UserRole.values();
        for (long i = 1; i <= availableRoles.length; i++) {
            var roleName = availableRoles[(int) (i - 1)].name();
            var role = roleRepository.existsByIdAndName(i, roleName)
                    ? roleRepository.findRoleByName(roleName)
                    : new Role(i, roleName);
            role.setPermissions(initPermission(roleName));
            roles.add(role);
        }

        if (roles.size() > 0) {
            roleRepository.saveAll(roles);
        }
    }

    private List<Permission> initPermission(String roleName) {
        return switch (roleName) {
            case "DUNGEON_MASTER" -> getPermissions(AvailablePermission.values());
            case "PLACE_OWNER" -> getPermissions(AvailablePermission.VIEW_DELIVERY,
                    AvailablePermission.VIEW_ORDER,
                    AvailablePermission.VIEW_MENU,
                    AvailablePermission.VIEW_PRODUCT,
                    AvailablePermission.VIEW_SITE,
                    AvailablePermission.MANAGE_PLACE_ROLE,
                    AvailablePermission.MANAGE_ROLE,
                    AvailablePermission.MANAGE_USER,
                    AvailablePermission.MANAGE_WORKER,
                    AvailablePermission.MANAGE_ORDER,
                    AvailablePermission.MANAGE_MENU,
                    AvailablePermission.MANAGE_PRODUCT,
                    AvailablePermission.MANAGE_SITE,
                    AvailablePermission.MANAGE_DELIVERY);
            default -> new ArrayList<>();
        };
    }

    private List<Permission> getPermissions(AvailablePermission... permissions) {
        List<Permission> result = new ArrayList<>();
        for (long i = 1; i <= permissions.length; i++) {
            var permission = permissions[(int) i - 1];
            var newPermission = permissionRepository.existsByName(permission.name())
                    ? permissionRepository.findByName(permission.name())
                    : new Permission(i, permission.name());
            result.add(newPermission);
        }
        return result;
    }
}
