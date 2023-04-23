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
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class RoleUtils {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        for (UserRole userRole : UserRole.values()) {
            Role role = getRoleByName(userRole.name());
            List<Permission> permissions = getRolePermission(userRole.name());
            if (comparingRolePermissionList(permissions, role.getPermissions())) {
                return;
            }
            insertNotContainingValues(role.getId(), role.getPermissions(), permissions);
            deleteNotContainingValues(role.getId(), role.getPermissions(), permissions);
        }
    }

    private Role getRoleByName(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            return new Role(roleName);
        }
        Role role = roleRepository.findRoleByName(roleName);
        role.setPermissions(permissionRepository.findByRoleId(role.getId()));
        return role;
    }

    private void insertNotContainingValues(long roleId, List<Permission> from, List<Permission> to) {
        for (Permission permission : to) {
            var isContains = from.stream().anyMatch(perm -> perm.getName().contains(permission.getName()));
            if (!isContains) {
                permissionRepository.insertRolePermission(roleId, permission.getId());
            }
        }
    }

    private void deleteNotContainingValues(long roleId, List<Permission> from, List<Permission> to) {
        for (Permission permission : from) {
            var isContains = to.stream().anyMatch(perm -> perm.getName().contains(permission.getName()));
            if (!isContains) {
                permissionRepository.removePermissionFromRole(roleId, permission.getId());
            }
        }
    }

    private boolean comparingRolePermissionList(List<Permission> list1, List<Permission> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        list1.sort(Comparator.comparing(Permission::getName));
        list2.sort(Comparator.comparing(Permission::getName));
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getName().equals(list2.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    private List<Permission> getRolePermission(String roleName) {
        return initRolePermissions(UserRole.valueOf(roleName).permissions);
    }

    private List<Permission> initRolePermissions(AvailablePermission... permissions) {
        List<Permission> result = new ArrayList<>();
        for (AvailablePermission permission : permissions) {
            var newPermission = permissionRepository.existsByName(permission.name())
                    ? permissionRepository.findByName(permission.name())
                    : new Permission(permission.name());
            result.add(newPermission);
        }
        return result;
    }
}
