package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Place;

import java.util.List;

public interface PlaceService {
    void createPlace(Place place);
    List<Place> getAllPlaces();
    void deletePlace(long placeId);
    void updatePlace(Place place);
}
