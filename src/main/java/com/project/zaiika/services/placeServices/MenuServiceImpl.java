package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.repositories.placesRepository.MenuRepository;
import com.project.zaiika.repositories.placesRepository.SiteRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final SiteRepository siteRepository;
    private final ContextService ctx;

    @Override
    public List<Menu> getAllMenus(long siteId) {
        checkPermission(siteId);
        validateSiteId(siteId);
        return menuRepository.findAllBySiteId(siteId);
    }

    @Override
    public void createMenu(Menu menu) {
        checkPermission(menu.getSiteId());
        validateSiteId(menu.getSiteId());
        menuRepository.save(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        checkPermission(menu.getSiteId());
        validateSiteId(menu.getSiteId());
        menuRepository.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        menuRepository.deleteMenuBySiteIdAndId(siteId, menuId);
    }

    private void checkPermission(long siteId) {
        var site = siteRepository.findSiteById(siteId);
        var place = ctx.getPlace();
        if (place.getId() != site.getPlaceId()){
            throw new IllegalArgumentException("permission denied");
        }
    }

    private void validateSiteId(long siteId) {
        if (!siteRepository.existsById(siteId)) {
            throw new IllegalArgumentException("site id '" + siteId + "' not exist");
        }
    }
}
