package com.project.zaiika.repositories;

import com.project.zaiika.models.permissio.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByIdAndName(long id, String name);
}
