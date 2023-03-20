package com.project.zaiika.controllers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.services.placeServices.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/site/{siteId}/menu")
@Slf4j
public class ManageMenuController {
    private final MenuService service;

    @Autowired
    public ManageMenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewMenu(@PathVariable("siteId") Long siteId,
                                           @RequestBody Menu menu) {
        try {
            return ResponseEntity.ok(service.createMenu(siteId, menu));
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId) {
        try {
            service.deleteMenu(siteId, menuId);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId,
                                        @RequestBody Menu menu) {
        try {
            menu.setId(menuId);
            service.updateMenu(siteId, menu);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
