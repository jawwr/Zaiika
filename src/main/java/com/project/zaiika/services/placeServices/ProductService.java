package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu(long menuId);

    Product addProductToMenu(long menuId, Product product);

    void updateProduct(long menuId, Product newProduct);

    void deleteProductById(long menuId, long productId);
}
