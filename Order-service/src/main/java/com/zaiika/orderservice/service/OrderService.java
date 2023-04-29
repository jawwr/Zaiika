package com.zaiika.orderservice.service;

import com.zaiika.orderservice.model.Order;

import java.util.List;

public interface OrderService {
    void createOrder(Order order);

    List<Order> getOrders();

    List<Order> getOrders(String type);

    void cancelOrder(long id);
}
