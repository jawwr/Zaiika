package com.project.zaiika.repositories.delivery;

import com.project.zaiika.models.order.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE deliveries
            SET type = :#{#delivery.deliveryType}
            WHERE id = :#{#delivery.id}
            """, nativeQuery = true)
    void updateDelivery(Delivery delivery);
}
