package com.project.zaiika.controllers;

import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.services.placeServices.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/menu/{menuId}")
@Slf4j
public class ManageProductController {
    private final ProductService service;

    @Autowired
    public ManageProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@PathVariable("menuId") Long menuId, @RequestBody Product product) {
        try {
            product.setMenuId(menuId);
            service.addProductToMenu(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("menuId") Long menuId, @PathVariable("productId") Long productId) {
        try {
            service.deleteProductById(menuId, productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/manage/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("menuId") Long menuId, @PathVariable("id") Long id, @RequestBody Product product) {
        try {
            product.setId(id);
            product.setMenuId(menuId);
            service.updateProduct(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
