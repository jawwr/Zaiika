package com.project.zaiika.services.delivery;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.order.Delivery;
import com.project.zaiika.models.order.DeliveryDto;
import com.project.zaiika.repositories.delivery.DeliveryRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ContextService ctx;

    @Override
    public void create(DeliveryDto deliveryDto) {
        var delivery = new Delivery();
        var place = ctx.getPlace();
        delivery.setPlaceId(place.getId());
        delivery.setDeliveryType(deliveryDto.name());

        deliveryRepository.save(delivery);
    }

    @Override
    public List<DeliveryDto> getAll() {
        var place = ctx.getPlace();
        var deliveries = deliveryRepository.findAllByPlaceId(place.getId());
        return deliveries.stream().map(d -> new DeliveryDto(d.getDeliveryType())).collect(Collectors.toList());
    }

    @Override
    public void deleteDelivery(long id) {
        checkPermission(id);
        deliveryRepository.deleteById(id);
    }

    @Override
    public void updateDelivery(long id, DeliveryDto deliveryDto) {
        checkPermission(id);
        var delivery = new Delivery();
        delivery.setDeliveryType(deliveryDto.name());
        delivery.setId(id);
        deliveryRepository.updateDelivery(delivery);
    }

    private void checkPermission(long id){
        var place = ctx.getPlace();
        var delivery = deliveryRepository.findDeliveryById(id);

        if (place.getId() != delivery.getPlaceId()){
            throw new PermissionDeniedException();
        }
    }
}
