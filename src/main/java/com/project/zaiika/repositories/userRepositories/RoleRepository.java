package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
