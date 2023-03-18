package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.models.placeModels.ProductModification;
import com.project.zaiika.models.placeModels.ProductModificationCategory;
import com.project.zaiika.repositories.placesRepository.IngredientRepository;
import com.project.zaiika.repositories.placesRepository.ProductModificationCategoryRepository;
import com.project.zaiika.repositories.placesRepository.ProductModificationRepository;
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
    private final ProductModificationRepository modificationRepository;
    private final ProductModificationCategoryRepository categoryRepository;
    private final ContextService ctx;

    @Override
    public List<Product> getAllProductFromMenu(long menuId) {
        checkPermission(menuId);

        return productRepository.findAllByMenuId(menuId);
    }

    @Override
    public Product addProductToMenu(Product product) {
        checkPermission(product.getMenuId());

        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Product updateProduct) {
        checkPermission(updateProduct.getMenuId());
        validateProductId(updateProduct.getId());

        updateIngredients(updateProduct.getComposition());
        updateModificationCategory(updateProduct.getModifications());
        productRepository.updateProduct(updateProduct);
    }

    private void updateModification(List<ProductModification> modifications) {
        for (ProductModification modification : modifications) {
            if (!modificationRepository.existsById(modification.getId())) {
                modificationRepository.save(modification);
            } else {
                modificationRepository.updateModification(modification);
            }
        }
    }

    private void updateModificationCategory(List<ProductModificationCategory> categories) {
        for (ProductModificationCategory category : categories) {
            if (!categoryRepository.existsById(category.getId())) {
                categoryRepository.save(category);
            } else {
                categoryRepository.updateCategory(category);
            }
            updateModification(category.getModification());
        }
    }

    private void updateIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (!ingredientRepository.existsById(ingredient.getId())) {
                ingredientRepository.save(ingredient);
            } else {
                ingredientRepository.updateProduct(ingredient);
            }
        }
    }

    @Override
    public void deleteProductById(long menuId, long productId) {
        checkPermission(menuId);
        validateProductId(productId);

        var product = productRepository.findProductById(productId);

        deleteCategories(product.getModifications());
        ingredientRepository.deleteIngredientsByProductId(productId);
        productRepository.deleteProductByMenuIdAndId(menuId, productId);
    }

    private void deleteCategories(List<ProductModificationCategory> categories) {
        for (ProductModificationCategory category : categories) {
            deleteModifications(category.getModification());
            categoryRepository.deleteById(category.getId());
        }
    }

    private void deleteModifications(List<ProductModification> modifications) {
        for (ProductModification modification : modifications) {
            modificationRepository.deleteById(modification.getId());
        }
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
