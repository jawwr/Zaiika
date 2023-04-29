package com.project.zaiika.repositories.role;

import com.project.zaiika.models.roles.PlaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaceRoleRepository extends JpaRepository<PlaceRole, Long> {
    @Modifying
    @Transactional
    void deleteRoleById(long roleId);

    @Query(value = """
            select *
            from place_roles
            where place_id = :#{#placeId}
            """, nativeQuery = true)
    List<PlaceRole> findAllByPlaceId(long placeId);

    PlaceRole findPlaceRoleById(long id);

    PlaceRole findPlaceRoleByPlaceIdAndNameIgnoreCase(long placeId, String name);
}