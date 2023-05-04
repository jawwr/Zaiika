package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.Menu;
import com.zaiika.placeservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
//    private final ContextService ctx;

    @Override
    public List<Menu> getAllMenus(long siteId) {
//        checkPermission(siteId);
        return menuRepository.findAllBySiteId(siteId);
    }

    @Override
    public Menu createMenu(long siteId, Menu menu) {
        checkPermission(siteId);
//        var site = ctx.getSite(siteId);
//        menu.setSite(site);
        return menuRepository.save(menu);
    }

    @Override
    public void updateMenu(long siteId, Menu menu) {
        checkPermission(siteId);
//        var site = ctx.getSite(siteId);
//        menu.setSite(site);
        menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        checkPermission(siteId);
        menuRepository.deleteMenuById(menuId);
    }

    private void checkPermission(long siteId) {
//        var site = ctx.getSite(siteId);
//        if (site == null) {
//            throw new PermissionDeniedException();
//        }
    }
}
