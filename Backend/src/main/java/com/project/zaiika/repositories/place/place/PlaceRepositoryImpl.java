package com.project.zaiika.repositories.place.place;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.placeModels.PlaceCache;
import com.project.zaiika.repositories.place.place.cache.PlaceRedisCacheRepository;
import com.project.zaiika.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepository {
    protected final PlaceJpaRepository jpaRepository;
    private final UserRepository userRepository;
    protected final PlaceRedisCacheRepository redisCacheRepository;

    @Override
    public Place getPlaceById(long id) {
        var cache = redisCacheRepository.getById(id);
        if (cache != null) {
            return convertPlaceCacheToPlace(cache);
        }
        var place = jpaRepository.findPlaceById(id);
        redisCacheRepository.save(convertPlaceToCache(place));
        return place;
    }

    @Override
    public boolean existById(long id) {
        var place = redisCacheRepository.getById(id);
        if (place != null) {
            return true;
        }
        return jpaRepository.existsById(id);
    }

    @Override
    public Place save(Place place) {
        var savedPlace = jpaRepository.save(place);
        redisCacheRepository.save(convertPlaceToCache(savedPlace));
        return savedPlace;
    }

    @Override
    public List<Place> getAllPlaces() {
        return jpaRepository.findAll();
    }

    @Override
    public void deletePlaceById(long id) {
        redisCacheRepository.delete(id);
        jpaRepository.deletePlaceById(id);
    }

    @Override
    public Place findPlaceByOwnerId(long ownerId) {
        return jpaRepository.findPlaceByOwnerId(ownerId);
    }

    private PlaceCache convertPlaceToCache(Place place) {
        return PlaceCache.builder()
                .id(place.getId())
                .name(place.getName())
                .ownerId(place.getOwner().getId())
                .build();
    }

    private Place convertPlaceCacheToPlace(PlaceCache cache) {
        return Place.builder()
                .id(cache.id())
                .name(cache.name())
                .owner(userRepository.getUserById(cache.ownerId()))
                .build();
    }
}
