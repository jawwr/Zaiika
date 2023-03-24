package com.project.zaiika.services.order;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.order.Order;
import com.project.zaiika.repositories.delivery.DeliveryRepository;
import com.project.zaiika.repositories.order.OrderRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ContextService ctx;
    private final DeliveryRepository deliveryService;

    @Override
    public void createOrder(Order order) {
        var place = ctx.getPlace();
        var user = ctx.getContextUser();
        order.setWorkerId(user.getId());
        order.setPlace(place);
        order.setDate(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders() {
        var place = ctx.getPlace();

        return orderRepository.findOrdersByPlaceId(place.getId());
    }

    @Override
    public List<Order> getOrders(String type) {
        var delivery = deliveryService.findDeliveryByDeliveryType(type);
        return orderRepository.findOrdersByDeliveryTypeId(delivery.getId());
    }

    @Override
    public void cancelOrder(long id) {
        var place = ctx.getPlace();
        var order = orderRepository.findOrderById(id);
        if (place.getId() != order.getPlace().getId()) {
            throw new PermissionDeniedException();
        }
        orderRepository.updateCancel(id);
    }
}
