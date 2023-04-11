package com.project.zaiika.services.permission;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.repositories.permissions.PermissionRepository;
import com.project.zaiika.repositories.role.PlaceRoleRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PlaceRoleRepository placeRoleRepository;
    private final ContextService ctx;

    @Override
    public List<Permission> getAllPermissions() {
        return new ArrayList<>();//todo
    }

    @Override
    public List<Permission> getRolePermissions(long roleId) {
        checkPermission(roleId);
        var role = placeRoleRepository.findPlaceRoleById(roleId);
        return role.getPermissions();
    }

    @Override
    public void setPermissions(long roleId, List<String> permissions) {
        checkPermission(roleId);
        var role = placeRoleRepository.findPlaceRoleById(roleId);
        List<Permission> newPermissions = new ArrayList<>();
        for (String permissionName : permissions) {
            var permission = permissionRepository.findByName(permissionName);
            if (permission == null) {
                throw new IllegalArgumentException("Wrong permission name");
            }
            newPermissions.add(permission);
        }
        role.setPermissions(newPermissions);
        placeRoleRepository.save(role);
    }

    private void checkPermission(long roleId) {
        var role = ctx.getPlaceRole(roleId);
        if (role == null) {
            throw new PermissionDeniedException();
        }
    }
}
