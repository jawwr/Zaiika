package com.project.zaiika.repositories.placesRepository;

import com.project.zaiika.models.placeModels.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Modifying
    @Transactional
    void deleteIngredientsByProductId(long productId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM ingredients
            WHERE id = :#{#id}
            """, nativeQuery = true)
    void deleteIngredientById(long id);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ingredients
            SET title = :#{#ingredient.title},
            net_weight = :#{#ingredient.netWeight},
            gross_weight = :#{#ingredient.grossWeight}
            WHERE id = :#{#ingredient.id}""", nativeQuery = true)
    void updateIngredient(Ingredient ingredient);
}
