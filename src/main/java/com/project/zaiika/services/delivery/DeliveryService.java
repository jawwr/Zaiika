package com.project.zaiika.services.delivery;

import com.project.zaiika.models.order.DeliveryDto;

import java.util.List;

public interface DeliveryService {
    void create(DeliveryDto delivery);

    List<DeliveryDto> getAll();

    void deleteDelivery(long id);

    void updateDelivery(long id, DeliveryDto deliveryDto);
}
