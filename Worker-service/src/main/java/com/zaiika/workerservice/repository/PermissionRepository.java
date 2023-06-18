package com.zaiika.workerservice.repository;

import com.zaiika.workerservice.model.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = """
            select count(*) <> 0
            from permissions
            where name = :#{#permissionName}
            """, nativeQuery = true)
    boolean isPermissionExists(String permissionName);
}
