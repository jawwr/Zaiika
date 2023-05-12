package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Place;

import java.util.List;

public interface PlaceService {
    Place createPlace(Place place);

    List<Place> getAllPlaces();

    void deletePlace(long placeId);

    void updatePlace(Place place);

    Place getPlace();

    Place getPlace(long id);
}
