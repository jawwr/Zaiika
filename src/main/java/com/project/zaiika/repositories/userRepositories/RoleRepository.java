package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);

    Role findRoleByName(String name);

    Role findRoleByNameIgnoreCase(String roleName);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE roles
            SET name = :#{#role.name}
            WHERE id = :#{#role.id}""", nativeQuery = true)
    void updateRole(Role role);
}
