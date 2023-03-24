package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findAllByPlaceId(long placeId);

    @Modifying
    @Transactional
    void deleteSiteById(long siteId);
}
