package com.project.zaiika.controllers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.order.Delivery;
import com.project.zaiika.services.delivery.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/delivery")
@Slf4j
public class ManageDeliveryController {
    private final DeliveryService service;

    @Autowired
    public ManageDeliveryController(DeliveryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getExistDelivery() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        try {
            service.create(delivery);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<?> updateDelivery(@PathVariable("deliveryId") Long id,
                                            @RequestBody Delivery delivery) {
        try {
            service.updateDelivery(id, delivery);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable("deliveryId") Long id) {
        try {
            service.deleteDelivery(id);
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
