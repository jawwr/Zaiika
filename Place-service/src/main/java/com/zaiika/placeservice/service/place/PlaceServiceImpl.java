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
    private final PlaceRepository repository;
    private final UserService userService;

    @Override
    public Place createPlace(Place place) {
        var user = userService.getUser();
        place.setOwnerId(user.id());
//        userService.setRoleToUser(user.id(), UserRole.PLACE_OWNER.name());//TODO
        return repository.save(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return repository.getAllPlaces();
    }

    @Override
    public void deletePlace(long placeId) {
//        var place = repository.getPlaceById(placeId);
//        userService.deleteRoleFromUser(place.getOwner().getId(), UserRole.PLACE_OWNER.name());//TODO
        repository.deletePlaceById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        var savedPlace = repository.getPlaceById(place.getId());
        place.setOwnerId(savedPlace.getOwnerId());
        repository.save(place);
    }
    //TODO check on relation with place
}
