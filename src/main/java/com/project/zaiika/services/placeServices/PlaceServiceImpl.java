package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;

    @Override
    public void createPlace(Place place) {
        repository.createPlace(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return repository.findAll();
    }

    @Override
    public void deletePlace(long placeId) {
        repository.deleteById(placeId);
    }

    @Override
    public void updatePlace(Place place) {
        repository.updatePlace(place);
    }
}
