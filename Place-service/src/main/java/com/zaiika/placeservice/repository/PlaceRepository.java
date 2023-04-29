package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.Place;

import java.util.List;

public interface PlaceRepository {
    Place getPlaceById(long id);

    boolean existById(long id);

    Place save(Place place);

    List<Place> getAllPlaces();

    void deletePlaceById(long id);

    Place findPlaceByOwnerId(long ownerId);
}
