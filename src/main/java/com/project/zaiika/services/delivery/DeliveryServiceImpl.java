package com.project.zaiika.services.delivery;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.delivery.Delivery;
import com.project.zaiika.repositories.delivery.DeliveryRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ContextService ctx;

    @Override
    public Delivery create(Delivery delivery) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManageDeliveryPermission);

        var place = ctx.getPlace();
        delivery.setPlace(place);

        return deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> getAll() {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isViewDeliveryPermission);

        var place = ctx.getPlace();
        return deliveryRepository.findAllByPlaceId(place.getId());
    }

    @Override
    public void deleteDelivery(long id) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManagePlaceRolePermission);

        checkPlacePermission(id);
        deliveryRepository.deleteById(id);
    }

    @Override
    public void updateDelivery(long id, Delivery delivery) {
        checkPlacePermission(id);
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManagePlaceRolePermission);

        delivery.setId(id);
        deliveryRepository.save(delivery);
    }

    private void checkPlacePermission(long id) {
        var place = ctx.getPlace();
        var delivery = deliveryRepository.findDeliveryById(id);

        if (place.getId() != delivery.getPlace().getId()) {
            throw new PermissionDeniedException();
        }
    }
}
