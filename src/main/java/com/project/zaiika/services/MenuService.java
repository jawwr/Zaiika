package com.project.zaiika.services;

import com.project.zaiika.models.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenus(long placeId);

    void createMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(Long siteId, Long menuId);
}
