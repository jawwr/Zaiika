package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.models.placeModels.ProductModification;
import com.project.zaiika.models.placeModels.ProductModificationCategory;
import com.project.zaiika.repositories.place.ingredients.IngredientRepository;
import com.project.zaiika.repositories.place.product.ProductModificationCategoryRepository;
import com.project.zaiika.repositories.place.product.ProductModificationRepository;
import com.project.zaiika.repositories.place.product.ProductRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductModificationRepository modificationRepository;
    private final ProductModificationCategoryRepository categoryRepository;
    private final ContextService ctx;

    @Override
    public List<Product> getAllProductFromMenu(long menuId) {
        checkPermission(menuId);

        return productRepository.findAllByMenuId(menuId);
    }

    @Override
    public Product addProductToMenu(long menuId, Product product) {
        checkPermission(menuId);
        var menu = ctx.getMenu(menuId);
        product.setMenu(menu);
        setDependencies(product);

        return productRepository.save(product);
    }

    private void setDependencies(Product product) {
        for (Ingredient ingredient : product.getComposition()) {
            ingredient.setProduct(product);
        }

        for (ProductModificationCategory category : product.getModifications()) {
            category.setProduct(product);
            for (ProductModification productModification : category.getModification()) {
                productModification.setCategory(category);
            }
        }
    }

    @Override
    public void updateProduct(long menuId, Product updateProduct) {
        checkPermission(menuId);
        validateProductId(updateProduct.getId());

        var menu = ctx.getMenu(menuId);
        updateProduct.setMenu(menu);
        setDependencies(updateProduct);
        productRepository.save(updateProduct);
    }

    @Override
    public void deleteProductById(long menuId, long productId) {
        checkPermission(menuId);
        validateProductId(productId);

        var product = productRepository.findProductById(productId);

        deleteCategories(product.getModifications());
        ingredientRepository.deleteIngredientsByProductId(productId);
        productRepository.deleteProductById(productId);
    }

    private void deleteCategories(List<ProductModificationCategory> categories) {
        for (ProductModificationCategory category : categories) {
            modificationRepository.deleteProductModificationsByCategoryId(category.getId());
            categoryRepository.deleteProductModificationCategoryById(category.getId());
        }
    }

    private void validateProductId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id '" + id + "'");
        }
    }

    private void checkPermission(long menuId) {
        try {
            ctx.getMenu(menuId);
        } catch (IllegalArgumentException e) {
            throw new PermissionDeniedException();
        }
    }
}