package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import com.project.zaiika.repositories.placesRepository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    private final SiteRepository siteRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository, PlaceRepository placeRepository) {
        this.siteRepository = siteRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public void createSite(Site site) {
        validatePlaceId(site.getPlaceId());
        siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites(long placeId) {
        validatePlaceId(placeId);
        return siteRepository.findAllByPlaceId(placeId);
    }

    @Override
    public void updateSite(Site site) {
        validatePlaceId(site.getPlaceId());
        siteRepository.updateSite(site);
    }

    @Override
    public void deleteSite(long siteId) {
        siteRepository.deleteSiteById(siteId);
    }

    private void validatePlaceId(long placeId){
        if (!placeRepository.existsPlaceById(placeId)){
            throw new IllegalArgumentException("place id '" + placeId + "' not exist");
        }
    }
}
