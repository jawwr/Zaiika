package com.project.zaiika.repositories.place;

import com.project.zaiika.models.placeModels.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findPlaceByOwnerId(long ownerId);

    Place findPlaceById(long id);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM places
            WHERE id = :#{#id}
            """, nativeQuery = true)
    void deletePlaceById(long id);
}
