package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.repositories.placesRepository.IngredientRepository;
import com.project.zaiika.repositories.placesRepository.ProductRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ContextService ctx;

    @Override
    public List<Product> getAllProductFromMenu(long menuId) {
        checkPermission(menuId);

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
        checkPermission(product.getMenuId());

        var saveProduct = productRepository.save(product);
        for (Ingredient ingredient : product.getComposition()) {
            ingredient.setProductId(saveProduct.getId());
        }

        ingredientRepository.saveAll(product.getComposition());
    }

    @Override
    public void updateProduct(Product updateProduct) {
        checkPermission(updateProduct.getMenuId());

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
        checkPermission(menuId);
        validateProductId(productId);

        productRepository.deleteProductByMenuIdAndId(menuId, productId);
        ingredientRepository.deleteIngredientsByProductId(productId);
    }

    private void validateProductId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id '" + id + "'");
        }
    }

    private void checkPermission(long menuId) {
        var menu = ctx.getMenu(menuId);
        if (menu == null) {
            throw new PermissionDeniedException();
        }
    }
}
