package com.project.zaiika.services;

import com.project.zaiika.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu(long menuId);

    void addProductToMenu(Product product);

    void updateProduct(Product newProduct);

    void deleteProductById(long id);
}
