package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Site;
import com.zaiika.placeservice.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {
    private final SiteRepository siteRepository;
    private final PlaceService placeService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "siteUser";

    @Override
    @Transactional
    public Site createSite(Site site) {
        var place = placeService.getPlace();
        site.setPlace(place);
        saveSiteToCache(site);
        return siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites() {
        var place = placeService.getPlace();
        return siteRepository.findAllByPlaceId(place.getId());
    }

    @Override
    @Transactional
    public void updateSite(Site site) {
        checkPermission(site.getId());

        var place = placeService.getPlace();
        site.setPlace(place);

        siteRepository.save(site);
        saveSiteToCache(site);
    }

    @Override
    public void deleteSite(long siteId) {
        checkPermission(siteId);
        siteRepository.deleteSiteById(siteId);
    }

    @Override
    public Site getSite(long id) {
        checkPermission(id);
        var place = placeService.getPlace();
        var cache = getSiteFromCache(id);
        if (cache != null && cache.getPlace().getId() == place.getId()) {
            return cache;
        }
        var sites = siteRepository.findAllByPlaceId(place.getId());
        var site = sites.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Site with id " + id + " does not exist"));
        saveSiteToCache(site);
        return site;
    }

    private Site getSiteFromCache(long siteId) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(siteId, Site.class);
    }

    private void saveSiteToCache(Site site) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        cache.put(site.getId(), site);
    }

    private void checkPermission(long siteId) {
        var place = placeService.getPlace();
        if (!siteRepository.isSiteExistsById(place.getId(), siteId)) {
            throw new IllegalArgumentException("Site does not exist");
        }
    }
}
