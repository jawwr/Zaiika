package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.PlaceRole;

import java.util.List;

public interface PlaceRoleService {
    void createRole(PlaceRole role);

    void deleteRole(long placeId, long roleId);

    void updateRole(PlaceRole role);

    List<PlaceRole> getAllRoles(long placeId);
}
