package com.project.zaiika.services;

import com.project.zaiika.models.Place;

import java.util.List;

public interface PlaceService {
    void createPlace(Place place);
    List<Place> getAllPlaces();
    void deletePlace(long placeId);
    void updatePlace(Place place);
}
