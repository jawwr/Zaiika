package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final SiteService siteService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "menu";

    @Override
    public List<Menu> getAllMenus(long siteId) {
        var site = siteService.getSite(siteId);
        return menuRepository.findAllBySiteId(site.getId());
    }

    @Override
    public Menu getMenu(long siteId, long menuId) {
        var site = siteService.getSite(siteId);
        var cache = getMenuFromCache(menuId);
        if (cache != null && site.getId() == cache.getSite().getId()) {
            return cache;
        }
        var menu = menuRepository.findMenuById(menuId);
        if (menu == null || site.getId() != menu.getSite().getId()) {
            throw new IllegalArgumentException("Menu with id " + menuId + " does not exist");
        }
        saveMenuToCache(menu);
        return menu;
    }

    @Override
    public Menu getMenu(long menuId) {
        var cache = getMenuFromCache(menuId);
        if (cache != null) {
            var siteId = cache.getSite().getId();
            return getMenu(siteId, cache.getId());
        }
        var savedMenu = menuRepository.findMenuById(menuId);
        if (savedMenu == null) {
            throw new IllegalArgumentException("Menu with id " + menuId + " does not exist");
        }
        saveMenuToCache(savedMenu);
        return getMenu(savedMenu.getSite().getId(), savedMenu.getId());
    }

    private Menu getMenuFromCache(long id) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(id, Menu.class);
    }

    private void saveMenuToCache(Menu menu) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        cache.put(menu.getId(), menu);
    }

    @Override
    @Transactional
    public Menu createMenu(long siteId, Menu menu) {
        var site = siteService.getSite(siteId);
        menu.setSite(site);
        saveMenuToCache(menu);
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public Menu updateMenu(long siteId, Menu menu) {
        var savedMenu = getMenu(siteId, menu.getId());
        menu.setSite(savedMenu.getSite());
        var updateMenu = menuRepository.save(menu);
        saveMenuToCache(updateMenu);
        return updateMenu;
    }

    @Override
    @Transactional
    public void deleteMenu(long siteId, long menuId) {
        var menu = getMenu(siteId, menuId);

        menuRepository.deleteMenuById(menuId);
    }
}
