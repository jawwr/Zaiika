package com.zaiika.workerservice.service.role;

import com.zaiika.workerservice.model.PlaceRole;

import java.util.List;

public interface PlaceRoleService {
    PlaceRole createRole(PlaceRole role);

    void deleteRole(long roleId);

    PlaceRole updateRole(PlaceRole role);

    List<PlaceRole> getAllRoles();
}
