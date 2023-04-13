package com.project.zaiika.repositories.permissions;

import com.project.zaiika.models.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = """
            select count(*) <> 0
            from permissions
            where name = :#{#name}""", nativeQuery = true)
    boolean existsByName(String name);

    Permission findByName(String name);

    @Query(value = """
            select permissions.*
            from permissions
            join role_permission rp
                on permissions.id = rp.permission_id
            where rp.role_id = :#{#roleId}
            """, nativeQuery = true)
    List<Permission> findByRoleId(long roleId);
}
