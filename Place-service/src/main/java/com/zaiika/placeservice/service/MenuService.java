package com.zaiika.placeservice.service;

import com.zaiika.placeservice.model.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenus(long placeId);

    Menu createMenu(long siteId, Menu menu);

    void updateMenu(long siteId, Menu menu);

    void deleteMenu(Long siteId, Long menuId);
}
