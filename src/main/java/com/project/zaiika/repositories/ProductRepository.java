package com.project.zaiika.repositories;

import com.project.zaiika.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "INSERT INTO products(title) VALUES(:#{#product.title}) RETURNING product_id", nativeQuery = true)
    Long saveProductAndReturnId(Product product);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET title = :#{#product.title} WHERE product_id = :#{#product.id}", nativeQuery = true)
    void updateProduct(Product product);
}
