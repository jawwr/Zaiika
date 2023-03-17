package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsPlaceById(long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO places(name) VALUES(:#{#place.name})", nativeQuery = true)
    void createPlace(Place place);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE places
            SET name = :#{#place.name}
            WHERE place_id = :#{#place.id}""", nativeQuery = true)
    void updatePlace(Place place);

    Place findPlaceByOwnerId(long ownerId);

    Place findPlaceById(long id);
}
