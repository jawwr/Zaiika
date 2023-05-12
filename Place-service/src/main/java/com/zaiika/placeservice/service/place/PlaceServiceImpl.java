package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Place;
import com.zaiika.placeservice.repository.PlaceRepository;
import com.zaiika.placeservice.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO caching
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final UserService userService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "placeUser";

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

    @Override
    public Place getPlace() {
        var user = userService.getUser();
        var cache = getPlaceFromCache(user.id());
        if (cache != null) {
            return cache;
        }
        var place = placeRepository.findPlaceByOwnerId(user.id());
        if (place == null) {
            throw new IllegalArgumentException("Place does not exist");
        }

        savePlaceToCache(place, user.id());
        return place;
    }

    private Place getPlaceFromCache(long userId) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(userId, Place.class);
    }

    private void savePlaceToCache(Place place, long userId) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        cache.put(userId, place);
    }

    private void checkPermission(long placeId) {
        var user = userService.getUser();
        var place = placeRepository.getPlaceById(placeId);
        if (user.id() != place.getOwnerId()) {
            throw new IllegalArgumentException("User is not owner for this place");
        }
    }
}
