package com.zaiika.workerservice.repository;

import com.zaiika.workerservice.model.PlaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;

public interface PlaceRoleRepository extends JpaRepository<PlaceRole, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            delete
            from place_role_permission
            where place_role_id = :#{#roleId} ;
            delete
            from place_roles
            where id = :#{#roleId} ;
            """, nativeQuery = true)
    void deleteRoleById(long roleId);


    @Transactional
    @Query(value = """
            select id, name, place_id
            from place_roles
            where place_id = :#{#placeId}
            """, nativeQuery = true)
    List<PlaceRole> getAllByPlaceId(long placeId);

    @Query(value = """
            select *
            from place_roles
            where id = :#{#roleId}
            """, nativeQuery = true)
    PlaceRole findPlaceRoleById(long roleId);

    @Query(value = """
            select *
            from place_roles
            where place_id = :#{#placeId}
              and upper(name) = upper(:#{#name})
            """, nativeQuery = true)
    PlaceRole findPlaceRoleByPlaceIdAndName(long placeId, String name);
}
