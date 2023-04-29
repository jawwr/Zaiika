package com.zaiika.userservice.repository;

import com.zaiika.userservice.model.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = """
            select *
            from roles
            where id = :#{#id}
            """, nativeQuery = true)
    Role findRoleById(long id);

    @Query(value = """
            select *
            from roles
            where roles.name = :#{#name}
            """, nativeQuery = true)
    Role findRoleByName(String name);

    @Query(value = """
            select count(*) <> 0
            from roles
            where name = :#{#name}
            """, nativeQuery = true)
    boolean existsByName(String name);

    @Query(value = """
            select *
            from roles
            join user_role
                on roles.id = user_role.role_id
            where user_id = :#{#userId}
            """, nativeQuery = true)
    List<Role> findUserRole(long userId);
}
