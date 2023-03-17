package com.project.zaiika.services.order;

import com.project.zaiika.models.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public void createOrder(Order order) {

    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }
}
