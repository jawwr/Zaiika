package com.zaiika.placeservice.service;

import com.zaiika.placeservice.model.Place;

import java.util.List;

public interface PlaceService {
    Place createPlace(Place place);

    List<Place> getAllPlaces();

    void deletePlace(long placeId);

    void updatePlace(Place place);
}
