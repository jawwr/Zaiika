package com.zaiika.placeservice.service.place;

import com.zaiika.placeservice.model.place.Ingredient;
import com.zaiika.placeservice.model.place.Product;
import com.zaiika.placeservice.model.place.ProductModification;
import com.zaiika.placeservice.model.place.ProductModificationCategory;
import com.zaiika.placeservice.repository.IngredientRepository;
import com.zaiika.placeservice.repository.ProductModificationCategoryRepository;
import com.zaiika.placeservice.repository.ProductModificationRepository;
import com.zaiika.placeservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductModificationRepository modificationRepository;
    private final ProductModificationCategoryRepository categoryRepository;
    private final MenuService menuService;
    private final CacheManager cacheManager;
    private static final String CACHE_PRODUCTS_NAME = "productsMenu";
    private static final String CACHE_PRODUCT_NAME = "product";


    @Override
    public List<Product> getAllProductFromMenu(long menuId) {
        var menu = menuService.getMenu(menuId);
        var cache = getProductsFromCache(menu.getId());
        if (cache != null) {
            return cache;
        }
        var products = productRepository.findAllByMenuId(menu.getId());
        saveProductsToCache(products, menuId);
        return products;
    }

    private List<Product> getProductsFromCache(long menuId) {
        var cache = cacheManager.getCache(CACHE_PRODUCTS_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(menuId, List.class);
    }

    private void saveProductsToCache(List<Product> products, long menuId) {
        var cache = cacheManager.getCache(CACHE_PRODUCTS_NAME);
        if (cache == null) {
            return;
        }
        cache.put(menuId, products);
    }

    @Override
    @Transactional
    public Product createProduct(long menuId, Product product) {
        var menu = menuService.getMenu(menuId);
        product.setMenu(menu);
        setDependencies(product);

        saveProductToCache(product);

        return productRepository.save(product);
    }

    private void setProductToIngredients(Product product) {
        if (product.getComposition() == null) {
            return;
        }
        for (Ingredient ingredient : product.getComposition()) {
            ingredient.setProduct(product);
        }
    }

    private void setProductToCategory(Product product) {
        if (product.getModifications() == null) {
            return;
        }
        for (ProductModificationCategory category : product.getModifications()) {
            category.setProduct(product);
            setCategoryToModifications(category);
        }
    }

    private void setCategoryToModifications(ProductModificationCategory category) {
        if (category.getModification() == null) {
            return;
        }
        for (ProductModification productModification : category.getModification()) {
            productModification.setCategory(category);
        }
    }

    private void setDependencies(Product product) {
        setProductToIngredients(product);
        setProductToCategory(product);
    }

    private Product getProductFromCache(long id) {
        var cache = cacheManager.getCache(CACHE_PRODUCT_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(id, Product.class);
    }

    private void saveProductToCache(Product product) {
        var cache = cacheManager.getCache(CACHE_PRODUCT_NAME);
        if (cache == null) {
            return;
        }
        cache.put(product.getId(), product);
    }

    @Override
    @Transactional
    public Product updateProduct(long menuId, Product updateProduct) {
        var product = getProduct(menuId, updateProduct.getId());
        updateProduct.setMenu(product.getMenu());
        setDependencies(updateProduct);
        var savedProduct = productRepository.save(updateProduct);
        saveProductToCache(savedProduct);
        return savedProduct;
    }

    @Override
    public void deleteProductById(long menuId, long productId) {
        var product = getProduct(menuId, productId);

        deleteCategories(product.getModifications());
        ingredientRepository.deleteIngredientsByProductId(productId);
        productRepository.deleteProductById(productId);
    }

    @Override
    public Product getProduct(long id) {
        var cacheProduct = getProductFromCache(id);
        if (cacheProduct != null) {
            var menuId = cacheProduct.getMenu().getId();
            return getProduct(menuId, cacheProduct.getId());
        }
        var product = productRepository.findProductById(id);
        saveProductToCache(product);
        return getProduct(product.getMenu().getId(), product.getId());
    }

    @Override
    @Transactional
    public Product getProduct(long menuId, long productId) {
        var menu = menuService.getMenu(menuId);
        var cacheProduct = getProductFromCache(productId);
        if (cacheProduct != null && cacheProduct.getMenu().getId() == menu.getId()) {
            return cacheProduct;
        }
        var product = productRepository.findProductById(productId);
        if (product == null || product.getMenu().getId() != menu.getId()) {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist");
        }
        saveProductToCache(product);

        return product;
    }

    private void deleteCategories(List<ProductModificationCategory> categories) {
        for (ProductModificationCategory category : categories) {
            modificationRepository.deleteProductModificationsByCategoryId(category.getId());
            categoryRepository.deleteProductModificationCategoryById(category.getId());
        }
    }
}