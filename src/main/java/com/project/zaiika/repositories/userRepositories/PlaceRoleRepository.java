package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.PlaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaceRoleRepository extends JpaRepository<PlaceRole, Long> {
    @Modifying
    @Transactional
    void deleteRoleById(long roleId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE place_roles
            SET name = :#{#role.name}
            WHERE id = :#{#role.id}""", nativeQuery = true)
    void updateRole(PlaceRole role);

    List<PlaceRole> findAllByPlaceId(long placeId);
}
