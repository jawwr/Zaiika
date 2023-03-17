package com.project.zaiika.controllers;

import com.project.zaiika.models.order.DeliveryDto;
import com.project.zaiika.services.placeServices.MenuService;
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
}
