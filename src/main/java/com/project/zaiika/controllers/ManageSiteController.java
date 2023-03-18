package com.project.zaiika.controllers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.services.placeServices.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/site")
@Slf4j
public class ManageSiteController {
    private final SiteService service;

    @Autowired
    public ManageSiteController(SiteService service) {
        this.service = service;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createSite(@RequestBody Site site) {
        try {
            service.createSite(site);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{siteId}")
    public ResponseEntity<?> updateSite(@PathVariable("siteId") Long siteId, @RequestBody Site site) {
        try {
            site.setId(siteId);
            service.updateSite(site);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{siteId}")
    public ResponseEntity<?> deleteSite(@PathVariable("siteId") Long siteId) {
        try {
            service.deleteSite(siteId);
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
