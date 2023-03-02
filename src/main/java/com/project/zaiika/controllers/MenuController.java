package com.project.zaiika.controllers;

import com.project.zaiika.models.Product;
import com.project.zaiika.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final ProductService service;

    @Autowired
    public MenuController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getMenu() {
        try {
            return ResponseEntity.ok(service.getAllProductFromMenu());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            service.addProductToMenu(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
