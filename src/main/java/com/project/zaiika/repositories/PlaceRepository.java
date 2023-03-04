package com.project.zaiika.repositories;

import com.project.zaiika.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsPlaceById(long id);
}
