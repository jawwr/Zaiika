package com.project.zaiika.services;

import com.project.zaiika.models.Site;

import java.util.List;

public interface SiteService {
    void createSite(Site site);
    List<Site> getAllSites(long placeId);
    void updateSite(Site site);
    void deleteSite(long siteId);
}
