package com.zaiika.placeservice.service;

import com.zaiika.placeservice.model.Site;

import java.util.List;

public interface SiteService {
    Site createSite(Site site);

    List<Site> getAllSites();

    void updateSite(Site site);

    void deleteSite(long siteId);
}
