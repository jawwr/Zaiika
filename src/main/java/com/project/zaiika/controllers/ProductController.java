package com.project.zaiika.controllers;

import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.services.placeServices.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu/{menuId}")
@Slf4j
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getMenu(@PathVariable("menuId") Long menuId) {
        try {
            return ResponseEntity.ok(service.getAllProductFromMenu(menuId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@PathVariable("menuId") Long menuId, @RequestBody Product product) {
        try {
            service.addProductToMenu(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("menuId") Long menuId, @PathVariable("id") Long id) {
        try {
            service.deleteProductById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("menuId") Long menuId, @PathVariable("id") Long id, @RequestBody Product product) {
        try {
            product.setId(id);
            service.updateProduct(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
