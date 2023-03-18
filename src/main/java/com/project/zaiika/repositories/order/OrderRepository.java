package com.project.zaiika.repositories.order;

import com.project.zaiika.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByPlaceId(long placeId);

    List<Order> findOrdersByPlaceIdAndDeliveryTypeId(long placeId, long type);

    Order findOrderById(long id);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE orders
            SET is_cancelled = true
            where id = :#{#orderId}
            """, nativeQuery = true)
    void updateCancel(long orderId);
}
