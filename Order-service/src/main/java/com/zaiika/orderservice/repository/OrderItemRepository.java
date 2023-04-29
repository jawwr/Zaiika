package com.zaiika.orderservice.repository;

import com.project.zaiika.models.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = """
            select *
            from order_items
            where order_id = :#{#orderId}
            """, nativeQuery = true)
    List<OrderItem> findAllItemsByOrderId(long orderId);
}
