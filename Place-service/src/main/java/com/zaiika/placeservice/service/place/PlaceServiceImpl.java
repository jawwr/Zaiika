package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Place;
import com.zaiika.placeservice.repository.PlaceRepository;
import com.zaiika.placeservice.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO caching
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final UserService userService;

    @Override
    public Place createPlace(Place place) {
        var user = userService.getUser();
        place.setOwnerId(user.id());
        userService.setRole("PLACE_OWNER");
        return placeRepository.save(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.getAllPlaces();
    }

    @Override
    public void deletePlace(long placeId) {
        checkPermission(placeId);
        userService.deleteRole("PLACE_OWNER");
        placeRepository.deletePlaceById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        checkPermission(place.getId());

        var savedPlace = placeRepository.getPlaceById(place.getId());
        place.setOwnerId(savedPlace.getOwnerId());
        placeRepository.save(place);
    }

    private void checkPermission(long placeId) {
        var user = userService.getUser();
        var place = placeRepository.getPlaceById(placeId);
        if (user.id() != place.getOwnerId()) {
            throw new IllegalArgumentException("User is not owner for this place");
        }
    }
}
