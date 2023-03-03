package com.project.zaiika.repositories;

import com.project.zaiika.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query(value = "SELECT * FROM ingredients WHERE product_id IN :#{#ids}", nativeQuery = true)
    List<Ingredient> findAllByProductId(List<Long> ids);

    List<Ingredient> findAllByProductId(long id);

    @Modifying
    @Transactional
    void deleteIngredientsByProductId(Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ingredients " +
            "SET title = :#{#ingredient.title}, " +
            "net_weight = :#{#ingredient.netWeight}, " +
            "gross_weight = :#{#ingredient.grossWeight} " +
            "WHERE ingredient_id = :#{#ingredient.id}", nativeQuery = true)
    void updateProduct(Ingredient ingredient);
}
