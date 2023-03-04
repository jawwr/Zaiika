package com.project.zaiika.controllers;

import com.project.zaiika.models.Menu;
import com.project.zaiika.services.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site/{siteId}")
@Slf4j
public class MenuController {
    private final MenuService service;

    @Autowired
    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllMenus(@PathVariable("siteId") Long placeId) {
        try {
            return ResponseEntity.ok(service.getAllMenus(placeId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewMenu(@PathVariable("siteId") Long siteId, @RequestBody Menu menu) {
        try {
            menu.setSiteId(siteId);
            service.createMenu(menu);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("siteId") Long siteId, @PathVariable("menuId") Long menuId) {
        try {
            service.deleteMenu(siteId, menuId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable("siteId") Long siteId, @PathVariable("menuId") Long menuId, @RequestBody Menu menu) {
        try {
            menu.setId(menuId);
            menu.setSiteId(siteId);
            service.updateMenu(menu);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
