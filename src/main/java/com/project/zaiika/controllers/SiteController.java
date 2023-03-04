package com.project.zaiika.controllers;

import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.services.placeServices.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/place/{placeId}")
@Slf4j
public class SiteController {
    private final SiteService service;

    @Autowired
    public SiteController(SiteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllSites(@PathVariable("placeId") Long placeId) {
        try {
            return ResponseEntity.ok(service.getAllSites(placeId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createSite(@PathVariable("placeId") Long placeId, @RequestBody Site site) {
        try {
            site.setPlaceId(placeId);
            service.createSite(site);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
