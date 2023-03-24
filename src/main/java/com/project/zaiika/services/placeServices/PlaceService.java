package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Place;

import java.util.List;

public interface PlaceService {
    Place createPlace(Place place);

    List<Place> getAllPlaces(boolean isFull);

    void deletePlace(long placeId);

    void updatePlace(Place place);
}
