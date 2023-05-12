package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Menu getMenu(long siteId, long menuId) {
        var site = siteService.getSite(siteId);
        var cache = getMenuFromCache(menuId);
        if (cache != null && site.getId() == cache.getSite().getId()) {
            return cache;
        }
        var menu = menuRepository.findMenuById(menuId);
        if (site.getId() != menu.getSite().getId()) {
            throw new IllegalArgumentException("Menu does not exist");
        }
        saveMenuToCache(menu);
        return menu;
    }

    @Override
    public Menu getMenu(long id) {
        var cache = getMenuFromCache(id);
        if (cache != null) {
            var siteId = cache.getSite().getId();
            return getMenu(siteId, cache.getId());
        }
        var menu = menuRepository.findMenuById(id);
        saveMenuToCache(menu);
        return getMenu(menu.getSite().getId(), menu.getId());
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
    public void updateMenu(long siteId, Menu menu) {
        var site = siteService.getSite(siteId);
        menu.setSite(site);
        menuRepository.save(menu);
        saveMenuToCache(menu);
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
