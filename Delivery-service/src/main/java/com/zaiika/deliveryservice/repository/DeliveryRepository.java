package com.zaiika.deliveryservice.repository;

import com.zaiika.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByPlaceId(long placeId);

    Delivery findDeliveryById(long id);

    Delivery findDeliveryByDeliveryType(String type);
}
