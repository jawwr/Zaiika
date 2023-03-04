package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.Role;
import com.project.zaiika.models.userModels.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);
    Role findRoleByName(UserRole roleName);
}
