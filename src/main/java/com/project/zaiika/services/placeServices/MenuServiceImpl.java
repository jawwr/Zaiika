package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
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
    private final ContextService ctx;

    @Override
    public List<Menu> getAllMenus(long siteId) {
        checkPermission(siteId);
        return menuRepository.findAllBySiteId(siteId);
    }

    @Override
    public Menu createMenu(long siteId, Menu menu) {
        checkPermission(siteId);
        var site = ctx.getSite(siteId);
        menu.setSite(site);
        return menuRepository.save(menu);
    }

    @Override
    public void updateMenu(long siteId, Menu menu) {
        checkPermission(siteId);
        menuRepository.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        checkPermission(siteId);
        menuRepository.deleteMenuBySiteIdAndId(siteId, menuId);
    }

    private void checkPermission(long siteId) {
        var site = ctx.getSite(siteId);
        if (site == null){
            throw new PermissionDeniedException();
        }
    }
}
