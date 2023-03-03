package com.project.zaiika.services;

import com.project.zaiika.models.Menu;
import com.project.zaiika.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository repository;

    @Autowired
    public MenuServiceImpl(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Menu> getAllMenus(long siteId) {
        return repository.findAllBySiteId(siteId);
    }

    @Override
    public void createMenu(Menu menu) {
        repository.save(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        repository.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Long siteId, Long menuId) {
        repository.deleteMenuBySiteIdAndId(siteId, menuId);
    }
}
