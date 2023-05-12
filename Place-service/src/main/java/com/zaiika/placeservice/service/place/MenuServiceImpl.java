package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.repository.MenuRepository;
import com.zaiika.placeservice.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final UserService userService;

    @Override
    public List<Menu> getAllMenus(long siteId) {
        checkPermission(siteId);
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
        var user = userService.getUser();

    }
}
