package com.zaiika.authservice.repository;

import com.zaiika.authservice.model.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = """
            select *
            from roles
            where roles.name = :#{#name}
            """, nativeQuery = true)
    Role findRoleByName(String name);

    @Query(value = """
            insert into user_role(user_id, role_id)
            values (:#{#userId}, :#{#role.id})
            """, nativeQuery = true)
    void setRoleToUser(long userId, Role role);
}
