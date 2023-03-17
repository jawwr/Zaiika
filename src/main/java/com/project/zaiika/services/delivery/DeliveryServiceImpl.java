package com.project.zaiika.services.delivery;

import com.project.zaiika.models.order.Delivery;
import com.project.zaiika.models.order.DeliveryDto;
import com.project.zaiika.repositories.delivery.DeliveryRepository;
import com.project.zaiika.services.util.ContextUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ContextUserService ctx;

    @Override
    public void create(DeliveryDto deliveryDto) {
        var delivery = new Delivery();
        var place = ctx.getPlaceByContext();
        delivery.setPlaceId(place.getId());
        delivery.setDeliveryType(deliveryDto.name());
    }

    @Override
    public List<DeliveryDto> getAll() {
        var deliveries = deliveryRepository.findAll();
        return deliveries.stream().map(d -> new DeliveryDto(d.getDeliveryType())).collect(Collectors.toList());
    }

    @Override
    public void deleteDelivery(long id) {
        deliveryRepository.deleteById(id);
    }

    @Override
    public void updateDelivery(long id, DeliveryDto deliveryDto) {
        var delivery = new Delivery();
        delivery.setId(id);
        deliveryRepository.updateDelivery(delivery);
    }
}
