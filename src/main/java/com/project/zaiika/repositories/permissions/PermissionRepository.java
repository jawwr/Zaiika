package com.project.zaiika.repositories.permissions;

import com.project.zaiika.models.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query(value = """
            insert into role_permission(role_id, permission_id)  values(:#{#roleId}, :#{#permissionId})
            """, nativeQuery = true)
    void insertRolePermission(long roleId, long permissionId);

    @Modifying
    @Transactional
    @Query(value = """
            delete from role_permission
            where role_id = :#{#roleId}
            and permission_id = :#{#permissionId}
            """, nativeQuery = true)
    void removePermissionFromRole(long roleId, long permissionId);

    @Query(value = """
            select distinct p.*
            from permissions p
            join role_permission rp
                on p.id = rp.permission_id
            join roles r
                on r.id = rp.role_id
            join user_role ur
                on ur.role_id = r.id
            where ur.user_id = :#{#userId}
            """, nativeQuery = true)
    List<Permission> getUserPermission(long userId);
}
