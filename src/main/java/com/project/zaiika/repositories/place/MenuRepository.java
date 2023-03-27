package com.project.zaiika.repositories.place;

import com.project.zaiika.models.placeModels.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllBySiteId(long siteId);

    @Modifying
    @Transactional
    void deleteMenuById(long menuId);
}
