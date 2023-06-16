package com.zaiika.workerservice.repository;

import com.zaiika.workerservice.model.PlaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaceRoleRepository extends JpaRepository<PlaceRole, Long> {
    @Modifying
    @Transactional
    void deleteRoleById(long roleId);

    @Query(value = """
            select *
            from place_roles
            where id = :#{#id}
            """, nativeQuery = true)
    PlaceRole findPlaceRoleById(long id);

    @Query(value = """
            select *
            from place_roles
            where place_id = :#{#placeId}
              and upper(name) = upper(:#{#name})
            """, nativeQuery = true)
    PlaceRole findPlaceRoleByPlaceIdAndName(long placeId, String name);
}
