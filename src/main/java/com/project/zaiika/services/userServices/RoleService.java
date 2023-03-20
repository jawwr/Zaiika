package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    void deleteRole(long id);

    void updateRole(Role role);

    Role createRole(Role role);

    Role getRole(long id);
}
