package com.project.zaiika.repositories.role;

import com.project.zaiika.models.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(long id);

    Role findRoleByName(String name);

    @Query(value = """
            select *
            from roles
            where roles.name = :#{#name}
            """, nativeQuery = true)
    Role findRoleByNameWithoutUsers(String name);

    @Query(value = """
            select count(*) <> 0
            from roles
            where name = :#{#name}
            """, nativeQuery = true)
    boolean existsByName(String name);
}
