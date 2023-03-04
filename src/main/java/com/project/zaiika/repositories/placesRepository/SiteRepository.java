package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Site;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findAllByPlaceId(long placeId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE sites SET place_id = :#{#site.placeId} WHERE site_id = :#{#site.id}", nativeQuery = true)
    void updateSite(Site site);

    void deleteSiteById(long siteId);
}
