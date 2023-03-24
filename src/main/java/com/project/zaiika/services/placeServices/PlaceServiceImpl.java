package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {//TODO сделать
    private final PlaceRepository repository;
    private final ContextService ctx;

    @Override
    public Place createPlace(Place place) {
        var user = ctx.getContextUser();
        place.setOwnerId(user.getId());
        return repository.save(place);
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
        repository.save(place);
    }
}
