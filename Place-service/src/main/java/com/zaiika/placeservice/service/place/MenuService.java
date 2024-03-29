package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenus(long placeId);

    Menu getMenu(long siteId, long id);

    Menu getMenu(long id);

    Menu createMenu(long siteId, Menu menu);

    Menu updateMenu(long siteId, Menu menu);

    void deleteMenu(long siteId, long menuId);
}
