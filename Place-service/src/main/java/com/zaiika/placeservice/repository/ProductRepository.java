package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.place.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByMenuId(long menuId);

    Product findProductById(long id);

    @Modifying
    @Transactional
    void deleteProductById(long productId);
}
