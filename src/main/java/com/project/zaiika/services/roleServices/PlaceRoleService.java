package com.project.zaiika.services.roleServices;

import com.project.zaiika.models.roles.PlaceRole;

import java.util.List;

public interface PlaceRoleService {
    void createRole(PlaceRole role);

    void deleteRole(long roleId);

    void updateRole(PlaceRole role);

    List<PlaceRole> getAllRoles();
}
