package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.repositories.placesRepository.MenuRepository;
import com.project.zaiika.repositories.placesRepository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final SiteRepository siteRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, SiteRepository siteRepository) {
        this.menuRepository = menuRepository;
        this.siteRepository = siteRepository;
    }

    @Override
    public List<Menu> getAllMenus(long siteId) {
        validateSiteId(siteId);
        return menuRepository.findAllBySiteId(siteId);
    }

    @Override
    public void createMenu(Menu menu) {
        validateSiteId(menu.getSiteId());
        menuRepository.save(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        validateSiteId(menu.getSiteId());
        menuRepository.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        menuRepository.deleteMenuBySiteIdAndId(siteId, menuId);
    }

    private void validateSiteId(long siteId) {
        if (!siteRepository.existsById(siteId)) {
            throw new IllegalArgumentException("site id '" + siteId + "' not exist");
        }
    }
}
