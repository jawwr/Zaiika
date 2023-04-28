package com.project.zaiika.repositories.place.menu;

import com.project.zaiika.models.placeModels.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllBySiteId(long siteId);

    @Modifying
    @Transactional
    void deleteMenuById(long menuId);

    @Query(value = """
            select *
            from menus
            where site_id in :#{#siteIds}
            """, nativeQuery = true)
    List<Menu> findAllBySiteIds(List<Long> siteIds);
}
