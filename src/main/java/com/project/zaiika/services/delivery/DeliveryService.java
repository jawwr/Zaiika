package com.project.zaiika.services.delivery;

import com.project.zaiika.models.delivery.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery create(Delivery delivery);

    List<Delivery> getAll();

    void deleteDelivery(long id);

    void updateDelivery(long id, Delivery deliveryDto);
}
