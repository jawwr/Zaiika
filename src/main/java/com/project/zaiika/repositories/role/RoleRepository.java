package com.project.zaiika.repositories.role;

import com.project.zaiika.models.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);

    Role findRoleByName(String name);

    boolean existsByIdAndName(long id, String name);
}
