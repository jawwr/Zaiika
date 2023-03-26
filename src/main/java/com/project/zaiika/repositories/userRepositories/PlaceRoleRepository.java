package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.PlaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaceRoleRepository extends JpaRepository<PlaceRole, Long> {
    @Modifying
    @Transactional
    void deleteRoleById(long roleId);

    List<PlaceRole> findAllByPlaceId(long placeId);

    PlaceRole findPlaceRoleById(long id);

    PlaceRole findPlaceRoleByPlaceIdAndNameIgnoreCase(long placeId, String name);
}
