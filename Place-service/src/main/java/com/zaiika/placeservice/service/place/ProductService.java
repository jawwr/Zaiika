package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu(long menuId);

    Product addProductToMenu(long menuId, Product product);

    void updateProduct(long menuId, Product newProduct);

    void deleteProductById(long menuId, long productId);
}
