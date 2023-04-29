package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.ProductModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ProductModificationRepository extends JpaRepository<ProductModification, Long> {
    @Modifying
    @Transactional
    void deleteProductModificationById(long id);

    @Modifying
    @Transactional
    void deleteProductModificationsByCategoryId(long id);
}
