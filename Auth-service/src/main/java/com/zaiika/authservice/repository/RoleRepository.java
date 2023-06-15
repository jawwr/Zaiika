package com.zaiika.authservice.repository;

import com.zaiika.authservice.model.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = """
            select *
            from roles
            where roles.name = :#{#name}
            """, nativeQuery = true)
    Role findRoleByName(String name);

    @Modifying
    @Transactional
    @Query(value = """
            insert into user_role(user_id, role_id)
            values (:#{#userId}, :#{#role.id})
            """, nativeQuery = true)
    void setRoleToUser(long userId, Role role);

    @Query(value = """
            select count(*) <> 0
            from roles
            join user_role ur
                on ur.role_id = roles.id
            where ur.user_id = :#{#userId}
              and roles.name = :#{#roleName}
            """, nativeQuery = true)
    boolean hasRole(long userId, String roleName);

    @Modifying
    @Transactional
    @Query(value = """
            delete from user_role
            where user_id = :#{#userId}
            """, nativeQuery = true)
    void deleteAllRolesFromUser(long userId);
}
