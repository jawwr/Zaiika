package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findPlaceByOwnerId(long ownerId);

    Place findPlaceById(long id);

    @Query(value = """
            SELECT *
            FROM places
            """, nativeQuery = true)
    List<Place> findOnlyPlaceById();
}
