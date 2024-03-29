package com.zaiika.deliveryservice.service;

import com.zaiika.deliveryservice.model.Delivery;
import com.zaiika.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
//    private final ContextService ctx;

    @Override
    public Delivery create(Delivery delivery) {
//        var place = ctx.getPlace();
//        delivery.setPlace(place);

        return deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> getAll() {
//        var place = ctx.getPlace();
//        return deliveryRepository.findAllByPlaceId(place.getId());
        return null;
    }

    @Override
    public void deleteDelivery(long id) {
        checkPlacePermission(id);
        deliveryRepository.deleteById(id);
    }

    @Override
    public void updateDelivery(long id, Delivery delivery) {
        checkPlacePermission(id);

        delivery.setId(id);
        deliveryRepository.save(delivery);
    }

    private void checkPlacePermission(long id) {
//        var place = ctx.getPlace();
//        var delivery = deliveryRepository.findDeliveryById(id);
//
//        if (place.getId() != delivery.getPlace().getId()) {
//            throw new PermissionDeniedException();
//        }
    }
}
