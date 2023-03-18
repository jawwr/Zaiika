package com.project.zaiika.services.order;

import com.project.zaiika.models.order.Order;

import java.util.List;

public interface OrderService {
    void createOrder(Order order);
    List<Order> getOrders();
    List<Order> getOrders(String type);
}
