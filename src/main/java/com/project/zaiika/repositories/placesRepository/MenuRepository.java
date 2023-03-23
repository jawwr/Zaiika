package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query(value = """
            SELECT *
            FROM menus
            WHERE site_id = :#{#siteId}
            """, nativeQuery = true)
    List<Menu> findAllBySiteId(long siteId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM menus
            WHERE site_id = :#{#siteId}
            and id = :#{#menuId}
            """, nativeQuery = true)
    void deleteMenuBySiteIdAndId(long siteId, long menuId);
}
