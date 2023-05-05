package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Site;

import java.util.List;

public interface SiteService {
    Site createSite(Site site);

    List<Site> getAllSites();

    void updateSite(Site site);

    void deleteSite(long siteId);
}
