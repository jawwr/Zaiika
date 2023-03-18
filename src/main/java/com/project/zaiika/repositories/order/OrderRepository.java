package com.project.zaiika.repositories.order;

import com.project.zaiika.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByPlaceId(long placeId);
    List<Order> findOrdersByPlaceIdAndDeliveryTypeId(long placeId, long type);
}
