package com.project.zaiika.services.placeServices;

import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.repositories.placesRepository.IngredientRepository;
import com.project.zaiika.repositories.placesRepository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;


    @Override
    public List<Product> getAllProductFromMenu(long menuId) {
        var products = productRepository.findAllByMenuId(menuId);

        List<Long> productsId = products.stream().map(Product::getId).toList();
        List<Ingredient> ingredients = ingredientRepository.findAllByProductId(productsId);

        for (var product : products) {
            List<Ingredient> productIngredient = ingredients.stream().filter(x -> x.getProductId() == product.getId()).toList();
            product.setComposition(productIngredient);
        }
        return products;
    }

    @Override
    public void addProductToMenu(Product product) {
        var saveProduct = productRepository.save(product);
        for (Ingredient ingredient : product.getComposition()) {
            ingredient.setProductId(saveProduct.getId());
        }

        ingredientRepository.saveAll(product.getComposition());
    }

    @Override
    public void updateProduct(Product updateProduct) {
        validateProductId(updateProduct.getId());

        for (Ingredient ingredient : updateProduct.getComposition()) {
            ingredient.setProductId(updateProduct.getId());
        }
        ingredientRepository.deleteIngredientsByProductId(updateProduct.getId());

        productRepository.updateProduct(updateProduct);
        ingredientRepository.saveAll(updateProduct.getComposition());
    }

    @Override
    public void deleteProductById(long menuId, long productId) {
        validateProductId(productId);

        productRepository.deleteProductByMenuIdAndId(menuId, productId);
        ingredientRepository.deleteIngredientsByProductId(productId);
    }

    private void validateProductId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id '" + id + "'");
        }
    }
}
