package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO
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
    public Menu getMenu(long siteId, long id) {
        var site = siteService.getSite(siteId);
        var cache = getMenuFromCache(id);
        if (cache != null && site.getId() == cache.getSite().getId()) {
            return cache;
        }
        var menu = menuRepository.findMenuById(id);
        if (site.getId() != menu.getSite().getId()) {
            throw new IllegalArgumentException("Menu does not exist");
        }
        saveMenuToCache(menu);
        return menu;
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
    public Menu createMenu(long siteId, Menu menu) {
        var site = siteService.getSite(siteId);
        menu.setSite(site);
        return menuRepository.save(menu);
    }

    @Override
    public void updateMenu(long siteId, Menu menu) {
        var site = siteService.getSite(siteId);
        menu.setSite(site);
        menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        var site = siteService.getSite(siteId);
        var menu = menuRepository.findMenuById(menuId);
        if (site.getId() != menu.getSite().getId()) {
            throw new IllegalArgumentException("Menu does not exist");
        }
        menuRepository.deleteMenuById(menuId);
    }
}
