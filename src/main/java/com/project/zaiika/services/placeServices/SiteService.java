package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Site;

import java.util.List;

public interface SiteService {
    Site createSite(Site site);

    List<Site> getAllSites();

    void updateSite(Site site);

    void deleteSite(long siteId);
}
