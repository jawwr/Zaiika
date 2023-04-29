package com.zaiika.deliveryservice.service;

import com.zaiika.deliveryservice.model.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery create(Delivery delivery);

    List<Delivery> getAll();

    void deleteDelivery(long id);

    void updateDelivery(long id, Delivery deliveryDto);
}
