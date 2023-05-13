package com.zaiika.workerservice.service.role;

import com.zaiika.workerservice.model.PlaceRole;
import com.zaiika.workerservice.repository.PlaceRoleRepository;
import com.zaiika.workerservice.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceRoleServiceImpl implements PlaceRoleService {
    private final PlaceRoleRepository roleRepository;
    private final WorkerRepository workerRepository;

    @Override
    public void createRole(PlaceRole role) {
//        var place = ctx.getPlace();
//        role.setPlace(place);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(long roleId) {
        checkPermission(roleId);

        roleRepository.deleteRoleById(roleId);
        var workers = workerRepository.findAllByPlaceRoleId(roleId);
        workers.forEach(x -> x.setPlaceRole(null));
        workerRepository.saveAll(workers);
    }

    @Override
    public void updateRole(PlaceRole role) {
        checkPermission(role.getId());
        var savedRole = roleRepository.findPlaceRoleById(role.getId());
        role.setPlaceId(savedRole.getPlaceId());

        roleRepository.save(role);
    }

    @Override
    public List<PlaceRole> getAllRoles() {
//        var place = ctx.getPlace();

        return new ArrayList<>();// roleRepository.findAllByPlaceId(place.getId());
    }

    private void checkPermission(long roleId) {
        var role = roleRepository.findPlaceRoleById(roleId);
//        var place = ctx.getPlace();

//        if (role == null || place.getId() != role.getPlace().getId()) {
//            throw new PermissionDeniedException();
//        }
    }
}
