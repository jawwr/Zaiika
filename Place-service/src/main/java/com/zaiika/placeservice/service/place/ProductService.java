package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu(long menuId);

    Product createProduct(long menuId, Product product);

    Product updateProduct(long menuId, Product newProduct);

    void deleteProductById(long menuId, long productId);

    Product getProduct(long id);

    Product getProduct(long menuId, long productId);
}
