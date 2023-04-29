package com.zaiika.placeservice.service;

import com.zaiika.placeservice.model.Place;
import com.zaiika.placeservice.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;
//    private final UserService userService;
//    private final ContextService ctx;

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
        checkPermission(placeId);

        var place = repository.getPlaceById(placeId);
//        userService.deleteRoleFromUser(place.getOwner().getId(), UserRole.PLACE_OWNER.name());
        repository.deletePlaceById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        checkPermission(place.getId());

        var savedPlace = repository.getPlaceById(place.getId());
//        place.setOwner(savedPlace.getOwner());
        repository.save(place);
    }

    private void checkPermission(long placeId) {
//        var user = ctx.getContextUser();
//        var place = ctx.getPlace();
//        var isMainAdmin = user.getRoles()
//                .stream()
//                .anyMatch(x -> x.getName().equals(UserRole.DUNGEON_MASTER.name()));
//        if (!isMainAdmin && (place == null || place.getId() != placeId)) {
//            throw new PermissionDeniedException();
//        }
    }
}
