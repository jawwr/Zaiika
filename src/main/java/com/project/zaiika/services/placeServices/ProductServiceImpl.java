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

import java.util.ArrayList;
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

        return productRepository.save(product);
    }

    @Override
    public void updateProduct(long menuId, Product updateProduct) {
        checkPermission(menuId);
        validateProductId(updateProduct.getId());

        var menu = ctx.getMenu(menuId);
        updateProduct.setMenu(menu);

//        var oldProduct = menu.getProducts()
//                .stream()
//                .filter(x -> x.getId() == updateProduct.getId())
//                .findFirst()
//                .get();

        productRepository.save(updateProduct);

//        updateProductIngredients(updateProduct.getComposition(), oldProduct.getComposition());
//        updateProductModificationCategory(updateProduct.getModifications(), oldProduct.getModifications());
    }

    private void updateProductModification(List<ProductModification> modifications, List<ProductModification> oldModification) {
        modifications.sort((a, b) -> (int) (a.getId() - b.getId()));
        oldModification.sort((a, b) -> (int) (a.getId() - b.getId()));

        var containsList = findContainsModification(modifications, oldModification);

        updateExistModification(modifications, containsList);
        deleteNotUsedModification(oldModification, containsList);
    }

    private void deleteNotUsedModification(List<ProductModification> oldModification, List<ProductModification> containsList) {
        for (ProductModification modification : oldModification) {
            var isContains = containsList.stream().anyMatch(x -> x.getId() == modification.getId());
            if (!isContains) {
                modificationRepository.deleteProductModificationById(modification.getId());
            }
        }
    }

    private void updateExistModification(List<ProductModification> modifications, List<ProductModification> containsList) {
        for (ProductModification modification : modifications) {
            if (containsList.contains(modification)) {
                modificationRepository.updateModification(modification);
            } else {
                modificationRepository.save(modification);
            }
        }
    }

    private List<ProductModification> findContainsModification(List<ProductModification> modifications, List<ProductModification> oldModification) {
        List<ProductModification> result = new ArrayList<>();
        for (ProductModification modification : modifications) {
            var isContains = oldModification.stream().anyMatch(x -> x.getId() == modification.getId());
            if (isContains) {
                result.add(modification);
            }
        }
        return result;
    }

    private void updateProductModificationCategory(List<ProductModificationCategory> categories, List<ProductModificationCategory> oldCategories) {
        categories.sort((a, b) -> (int) (a.getId() - b.getId()));
        oldCategories.sort((a, b) -> (int) (a.getId() - b.getId()));

        var containsList = findContainsCategory(categories, oldCategories);

        updateCategory(categories, containsList, oldCategories);
        deleteNotUsedCategories(oldCategories, containsList);
    }

    private void deleteNotUsedCategories(List<ProductModificationCategory> oldCategories, List<ProductModificationCategory> containsList) {
        for (ProductModificationCategory category : oldCategories) {
            var isContains = containsList.stream().anyMatch(x -> x.getId() == category.getId());
            if (!isContains) {
                deleteModifications(category.getModification());
                categoryRepository.deleteProductModificationCategoryById(category.getId());
            }
        }
    }

    private void updateCategory(List<ProductModificationCategory> categories, List<ProductModificationCategory> containsList, List<ProductModificationCategory> oldCategories) {
        for (ProductModificationCategory category : categories) {
            if (containsList.contains(category)) {
                categoryRepository.updateCategory(category);
                var oldCategory = oldCategories.stream()
                        .filter(x -> x.getId() == category.getId())
                        .findFirst()
                        .get();
                updateProductModification(category.getModification(), oldCategory.getModification());
            } else {
                categoryRepository.save(category);
                updateExistModification(category.getModification(), new ArrayList<>());
            }
        }
    }

    private List<ProductModificationCategory> findContainsCategory(List<ProductModificationCategory> categories, List<ProductModificationCategory> oldCategories) {
        List<ProductModificationCategory> result = new ArrayList<>();
        for (ProductModificationCategory category : categories) {
            var isContains = oldCategories.stream().anyMatch(x -> x.getId() == category.getId());
            if (isContains) {
                result.add(category);
            }
        }

        return result;
    }

    private void updateProductIngredients(List<Ingredient> ingredients, List<Ingredient> oldIngredients) {
        ingredients.sort((a, b) -> (int) (a.getId() - b.getId()));
        oldIngredients.sort((a, b) -> (int) (a.getId() - b.getId()));

        var containsList = findContainsIngredients(ingredients, oldIngredients);

        updateExistIngredients(ingredients, containsList);
        deleteNotUsedIngredients(oldIngredients, containsList);
    }

    private void deleteNotUsedIngredients(List<Ingredient> ingredients, List<Ingredient> containsList) {
        for (Ingredient oldIngredient : ingredients) {
            var isContains = containsList.stream().anyMatch(x -> x.getId() == oldIngredient.getId());
            if (!isContains) {
                ingredientRepository.deleteIngredientById(oldIngredient.getId());
            }
        }
    }

    private void updateExistIngredients(List<Ingredient> ingredients, List<Ingredient> containsList) {
        for (Ingredient ingredient : ingredients) {
            var isContains = containsList.contains(ingredient);
            if (isContains) {
                ingredientRepository.save(ingredient);
            } else {
                ingredientRepository.save(ingredient);
            }
        }
    }

    private List<Ingredient> findContainsIngredients(List<Ingredient> ingredients, List<Ingredient> oldIngredients) {
        List<Ingredient> result = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            var isContains = oldIngredients.stream().anyMatch(x -> x.getId() == ingredient.getId());
            if (isContains) {
                result.add(ingredient);
            }
        }
        return result;
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
            deleteModifications(category.getModification());
            categoryRepository.deleteProductModificationCategoryById(category.getId());
        }
    }

    private void deleteModifications(List<ProductModification> modifications) {
        for (ProductModification modification : modifications) {
            modificationRepository.deleteProductModificationById(modification.getId());
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