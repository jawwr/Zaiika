package com.zaiika.orderservice.service.order;

import com.zaiika.orderservice.model.Order;
import com.zaiika.orderservice.model.OrderItem;
import com.zaiika.orderservice.repository.OrderItemRepository;
import com.zaiika.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {//TODO
    private final OrderRepository orderRepository;
    //    private final DeliveryRepository deliveryService;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void createOrder(Order order) {
//        var place = ctx.getPlace();
//        var user = ctx.getContextWorker();
//        order.setWorker(user);
//        order.setPlace(place);
        order.setDate(LocalDateTime.now());
        List<OrderItem> items = orderItemRepository.findAllItemsByOrderId(order.getId());

        for (OrderItem orderItem : items) {
            orderItem.setOrder(order);
        }

        orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders() {
//        var place = ctx.getPlace();
//
//        return orderRepository.findOrdersByPlaceId(place.getId());
        return null;
    }

    @Override
    public List<Order> getOrders(String type) {
//        var delivery = deliveryService.findDeliveryByDeliveryType(type);
//        return orderRepository.findOrdersByDeliveryId(delivery.getId());
        return null;
    }

    @Override
    public void cancelOrder(long id) {
//        var place = ctx.getPlace();
//        var order = orderRepository.findOrderById(id);
//        if (place.getId() != order.getPlace().getId()) {
//            throw new PermissionDeniedException();
//        }
        orderRepository.updateCancel(id);
    }
}
