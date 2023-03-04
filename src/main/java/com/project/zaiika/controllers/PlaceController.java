package com.project.zaiika.controllers;

import com.project.zaiika.services.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/placemanager")
@Slf4j
public class PlaceController {
    private final PlaceService service;

    @Autowired
    public PlaceController(PlaceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        try {
            return ResponseEntity.ok(service.getAllPlaces());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
