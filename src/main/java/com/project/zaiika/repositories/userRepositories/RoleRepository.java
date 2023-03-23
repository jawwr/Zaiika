package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);

    Role findRoleByName(String name);

    Role findRoleByNameIgnoreCase(String roleName);
}
