package com.project.zaiika.services.roleServices;

import com.project.zaiika.models.roles.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    void deleteRole(long id);

    void updateRole(Role role);

    Role createRole(Role role);

    Role getRole(long id);
}
