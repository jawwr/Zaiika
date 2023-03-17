package com.project.zaiika.repositories.placesRepository;

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
    void deleteMenuBySiteIdAndId(long siteId, long menuId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE menus
            SET title = :#{#menu.title}
            WHERE id = :#{#menu.id}""", nativeQuery = true)
    void updateMenu(Menu menu);

    Menu findBySiteId(long siteId);

    @Query(value = """
            SELECT *
            FROM menus
            WHERE site_id IN :#{#ids}
            """, nativeQuery = true)
    List<Menu> getAllBySiteIds(List<Long> ids);
}
