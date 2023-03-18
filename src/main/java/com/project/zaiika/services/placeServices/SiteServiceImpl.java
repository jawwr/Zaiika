package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import com.project.zaiika.repositories.placesRepository.SiteRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {
    private final SiteRepository siteRepository;
    private final PlaceRepository placeRepository;
    private final ContextService ctx;

    @Override
    public void createSite(Site site) {
        validatePlaceId(site.getPlaceId());
        siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites() {
        var place = ctx.getPlace();
        validatePlaceId(place.getId());
        return siteRepository.findAllByPlaceId(place.getId());
    }

    @Override
    public void updateSite(Site site) {
        var place = ctx.getPlace();
        site.setPlaceId(place.getId());

        validatePlaceId(site.getPlaceId());
        siteRepository.updateSite(site);
    }

    @Override
    public void deleteSite(long siteId) {
        var site = siteRepository.findSiteById(siteId);
        var place = ctx.getPlace();
        if (place.getId() != site.getPlaceId()){
            throw new PermissionDeniedException();
        }
        siteRepository.deleteSiteById(siteId);
    }

    private void validatePlaceId(long placeId) {
        if (!placeRepository.existsPlaceById(placeId)) {
            throw new IllegalArgumentException("place id '" + placeId + "' not exist");
        }
    }
}
