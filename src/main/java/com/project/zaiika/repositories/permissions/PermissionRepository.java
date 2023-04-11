package com.project.zaiika.repositories.permissions;

import com.project.zaiika.models.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByIdAndName(long id, String name);
    boolean existsByName(String name);
    Permission findByName(String name);
}
