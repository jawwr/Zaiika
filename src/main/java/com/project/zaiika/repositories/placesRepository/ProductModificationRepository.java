package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.ProductModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductModificationRepository extends JpaRepository<ProductModification, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE product_modification
            SET title = :#{#modification.title},
            price = :#{#modification.price}
            WHERE id = :#{#modification.id}
            """, nativeQuery = true)
    void updateModification(ProductModification modification);

    @Modifying
    @Transactional
    void deleteAllByCategory_Id(long id);

    @Modifying
    @Transactional
    void deleteById(long id);
}
