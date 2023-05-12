package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Site;
import com.zaiika.placeservice.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {
    private final SiteRepository siteRepository;
    private final PlaceService placeService;

    @Override
    public Site createSite(Site site) {
        var place = placeService.getPlace();
        site.setPlace(place);
        return siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites() {
        var place = placeService.getPlace();
        return siteRepository.findAllByPlaceId(place.getId());
    }

    @Override
    public void updateSite(Site site) {
        checkPermission(site.getId());

        var place = placeService.getPlace();
        site.setPlace(place);

        siteRepository.save(site);
    }

    @Override
    public void deleteSite(long siteId) {
        checkPermission(siteId);
        siteRepository.deleteSiteById(siteId);
    }

    private void checkPermission(long siteId) {
        var place = placeService.getPlace();
        if (siteRepository.isSiteExistById(place.getId(), siteId)) {
            throw new IllegalArgumentException("Site does not exist");
        }
    }
}
