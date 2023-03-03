package com.project.zaiika.repositories;

import com.project.zaiika.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query(value = "SELECT * FROM ingredients WHERE product_id IN :#{#ids}", nativeQuery = true)
    List<Ingredient> findAllByProductId(List<Long> ids);
}
