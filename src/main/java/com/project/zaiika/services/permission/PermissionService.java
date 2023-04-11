package com.project.zaiika.services.permission;

import com.project.zaiika.models.permission.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();

    List<Permission> getRolePermissions(long roleId);

    void setPermissions(long roleId, List<String> permissions);
}
