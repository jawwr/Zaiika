package com.project.zaiika.controllers;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.services.placeServices.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-place")
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

    @PostMapping("/create")
    public ResponseEntity<?> createPlace(@RequestBody Place place) {
        try {
            service.createPlace(place);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable("placeId") Long placeId) {
        try {
            service.deletePlace(placeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<?> updatePlace(@PathVariable("placeId") Long placeId,
                                         @RequestBody Place place) {
        try {
            place.setId(placeId);
            service.updatePlace(place);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}