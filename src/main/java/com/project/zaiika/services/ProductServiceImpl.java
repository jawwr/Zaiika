package com.project.zaiika.services;

import com.project.zaiika.models.Ingredient;
import com.project.zaiika.models.Product;
import com.project.zaiika.repositories.IngredientRepository;
import com.project.zaiika.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Product> getAllProductFromMenu() {
        var products = productRepository.findAll();
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
        long productId = productRepository.saveProductAndReturnId(product);
        for (Ingredient ingredient : product.getComposition()) {
            ingredient.setProductId(productId);
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
    public void deleteProductById(long id) {
        validateProductId(id);

        productRepository.deleteById(id);
        ingredientRepository.deleteIngredientsByProductId(id);
    }

    private void validateProductId(long id){
        if (id <= 0){
            throw new IllegalArgumentException("Invalid product id '" + id + "'");
        }
    }
}
