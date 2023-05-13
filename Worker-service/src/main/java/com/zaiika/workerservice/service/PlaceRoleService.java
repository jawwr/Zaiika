package com.zaiika.workerservice.service;

import com.zaiika.workerservice.model.PlaceRole;

import java.util.List;

public interface PlaceRoleService {
    void createRole(PlaceRole role);

    void deleteRole(long roleId);

    void updateRole(PlaceRole role);

    List<PlaceRole> getAllRoles();
}
