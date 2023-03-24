package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findAllByPlaceId(long placeId);

    void deleteSiteById(long siteId);
}
