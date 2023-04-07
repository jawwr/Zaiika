package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.repositories.place.PlaceRepository;
import com.project.zaiika.repositories.role.RoleRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {//TODO сделать
    private final PlaceRepository placeRepository;
    private final RoleRepository roleRepository;
    private final ContextService ctx;

    @Override
    public Place createPlace(Place place) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManagePlacePermission);

        var user = ctx.getContextUser();
        place.setOwner(user);
        setPlaceOwnerRole(user);
        return placeRepository.save(place);
    }

    private void setPlaceOwnerRole(User user) {//TODO сделать при добавлении владельца добавление работника со всеми привилегиями
        var roles = user.getRoles();
        var role = roleRepository.findRoleByName(UserRole.PLACE_OWNER.name());
        roles.add(role);
    }

    @Override
    public List<Place> getAllPlaces() {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isViewPlacePermission);

        return placeRepository.findAll();
    }

    @Override
    public void deletePlace(long placeId) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManagePlacePermission);

        checkPermission(placeId);

        var place = placeRepository.findPlaceById(placeId);
        deletePlaceOwnerRole(place.getOwner());
        placeRepository.deletePlaceById(placeId);
    }

    private void deletePlaceOwnerRole(User user) {
        var roles = user.getRoles();
        var deleteRole = roles.stream()
                .filter(x -> x.getName().equals(UserRole.PLACE_OWNER.name()))
                .findFirst()
                .get();

        roles.remove(deleteRole);
    }

    @Override
    public void updatePlace(Place place) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManagePlacePermission);

        checkPermission(place.getId());

        var savedPlace = placeRepository.findPlaceById(place.getId());
        place.setOwner(savedPlace.getOwner());
        placeRepository.save(place);
    }

    private void checkPermission(long placeId) {
        var user = ctx.getContextUser();
        var place = user.getPlace();
        var isMainAdmin = user.getRoles()
                .stream()
                .anyMatch(x -> x.getName().equals(UserRole.DUNGEON_MASTER.name()));
        if (!isMainAdmin && (place == null || place.getId() != placeId)) {
            throw new PermissionDeniedException();
        }
    }
}
