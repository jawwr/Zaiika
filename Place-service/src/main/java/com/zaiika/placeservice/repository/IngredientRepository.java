package com.zaiika.placeservice.repository;

import com.zaiika.placeservice.model.place.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Modifying
    @Transactional
    void deleteIngredientsByProductId(long productId);
}
