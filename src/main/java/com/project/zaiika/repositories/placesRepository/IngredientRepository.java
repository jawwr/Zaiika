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
    void deleteIngredientById(long id);
}
