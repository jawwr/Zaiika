package com.project.zaiika.repositories;

import com.project.zaiika.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "INSERT INTO products(title) VALUES(:#{#a.title}) RETURNING product_id", nativeQuery = true)
    Long saveProductAndReturnId(Product a);
}
