package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = """
            SELECT *
            FROM products
            WHERE menu_id = :#{#menuId}
            """, nativeQuery = true)
    List<Product> findAllByMenuId(long menuId);

    Product findProductById(long id);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM products
            WHERE menu_id = :#{#menuId}
            and id = :#{#productId}
            """, nativeQuery = true)
    void deleteProductByMenuIdAndId(long menuId, long productId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE products
            SET title = :#{#product.title}
            WHERE id = :#{#product.id}""", nativeQuery = true)
    void updateProduct(Product product);
}
