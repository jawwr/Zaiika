package com.project.zaiika.repositories.place.site;

import com.project.zaiika.models.placeModels.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findAllByPlaceId(long placeId);

    @Modifying
    @Transactional
    void deleteSiteById(long siteId);

    @Query(value = """
            select *
            from sites
            where place_id = :#{#placeId}
            """, nativeQuery = true)
    List<Site> findSitesByPlaceId(long placeId);
}
