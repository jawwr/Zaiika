package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.Place;
import com.zaiika.placeservice.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;

    @Override
    public Place createPlace(Place place) {
//        User user = ctx.getContextUser();
//        place.setOwner(user);
//        userService.setRoleToUser(user.getId(), UserRole.PLACE_OWNER.name());
        return repository.save(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return repository.getAllPlaces();
    }

    @Override
    public void deletePlace(long placeId) {
        var place = repository.getPlaceById(placeId);
//        userService.deleteRoleFromUser(place.getOwner().getId(), UserRole.PLACE_OWNER.name());
        repository.deletePlaceById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        var savedPlace = repository.getPlaceById(place.getId());
        place.setOwnerId(savedPlace.getOwnerId());
        repository.save(place);
    }
}
