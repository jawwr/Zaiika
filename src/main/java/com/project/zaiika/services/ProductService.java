package com.project.zaiika.services;

import com.project.zaiika.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu();
    void addProductToMenu(Product product);
    void changeProduct(Product newProduct);
    void deleteProductById(long id);
}
