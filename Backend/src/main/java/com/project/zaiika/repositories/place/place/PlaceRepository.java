package com.project.zaiika.repositories.place.place;

import com.project.zaiika.models.placeModels.Place;

import java.util.List;

public interface PlaceRepository {
    Place getPlaceById(long id);

    boolean existById(long id);

    Place save(Place place);

    List<Place> getAllPlaces();

    void deletePlaceById(long id);

    Place findPlaceByOwnerId(long ownerId);
}
