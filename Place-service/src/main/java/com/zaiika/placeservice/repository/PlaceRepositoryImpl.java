package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.place.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO caching
@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepository {
    protected final PlaceJpaRepository jpaRepository;

    @Override
    public Place getPlaceById(long id) {
        return jpaRepository.findPlaceById(id);
    }

    @Override
    public boolean existById(long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Place save(Place place) {
        return jpaRepository.save(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return jpaRepository.findAll();
    }

    @Override
    public void deletePlaceById(long id) {
        jpaRepository.deletePlaceById(id);
    }

    @Override
    public Place findPlaceByOwnerId(long ownerId) {
        return jpaRepository.findPlaceByOwnerId(ownerId);
    }
}
