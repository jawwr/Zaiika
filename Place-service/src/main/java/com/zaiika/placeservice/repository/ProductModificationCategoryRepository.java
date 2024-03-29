package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.place.ProductModificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ProductModificationCategoryRepository extends JpaRepository<ProductModificationCategory, Long> {
    @Modifying
    @Transactional
    void deleteProductModificationCategoryById(long id);
}
