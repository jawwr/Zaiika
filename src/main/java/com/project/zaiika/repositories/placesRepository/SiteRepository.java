package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    @Query(value = """
            SELECT *
            FROM sites
            WHERE place_id = :#{#placeId}
            """, nativeQuery = true)
    List<Site> findAllByPlaceId(long placeId);

    void deleteSiteById(long siteId);
}
