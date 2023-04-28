package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.models.user.User;
import com.project.zaiika.repositories.place.place.PlaceRepository;
import com.project.zaiika.services.userServices.UserService;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;
    private final UserService userService;
    private final ContextService ctx;

    @Override
    public Place createPlace(Place place) {
        User user = ctx.getContextUser();
        place.setOwner(user);
        userService.setRoleToUser(user.getId(), UserRole.PLACE_OWNER.name());
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
        userService.deleteRoleFromUser(place.getOwner().getId(), UserRole.PLACE_OWNER.name());
        repository.deletePlaceById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        checkPermission(place.getId());

        var savedPlace = repository.getPlaceById(place.getId());
        place.setOwner(savedPlace.getOwner());
        repository.save(place);
    }

    private void checkPermission(long placeId) {
        var user = ctx.getContextUser();
        var place = ctx.getPlace();
        var isMainAdmin = user.getRoles()
                .stream()
                .anyMatch(x -> x.getName().equals(UserRole.DUNGEON_MASTER.name()));
        if (!isMainAdmin && (place == null || place.getId() != placeId)) {
            throw new PermissionDeniedException();
        }
    }
}
