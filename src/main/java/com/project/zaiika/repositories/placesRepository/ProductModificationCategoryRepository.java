package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.ProductModificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductModificationCategoryRepository extends JpaRepository<ProductModificationCategory, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE modification_category
            SET title = :#{#category.title}
            WHERE id = :#{#category.id}
            """, nativeQuery = true)
    void updateCategory(ProductModificationCategory category);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM modification_category
            WHERE id = :#{#id}
            """, nativeQuery = true)
    void deleteProductModificationCategoryById(long id);
}
