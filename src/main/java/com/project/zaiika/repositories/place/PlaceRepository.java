package com.project.zaiika.repositories.place;

import com.project.zaiika.models.placeModels.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findPlaceByOwnerId(long ownerId);

    Place findPlaceById(long id);
}
