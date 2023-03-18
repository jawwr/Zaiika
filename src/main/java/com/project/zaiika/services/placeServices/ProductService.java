package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductFromMenu(long menuId);

    Product addProductToMenu(Product product);

    void updateProduct(Product newProduct);

    void deleteProductById(long menuId, long productId);
}
