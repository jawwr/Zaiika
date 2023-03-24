package com.project.zaiika.services.userServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.repositories.userRepositories.PlaceRoleRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceRoleServiceImpl implements PlaceRoleService {
    private final PlaceRoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final ContextService ctx;

    @Override
    public void createRole(PlaceRole role) {
        var place = ctx.getPlace();
        role.setPlace(place);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(long roleId) {
        checkPermission(roleId);

        roleRepository.deleteRoleById(roleId);
        var workers = workerRepository.findAllByPlaceRoleId(roleId);
        for (Worker worker : workers) {
            worker.setPlaceRoleId(0);
        }
        workerRepository.saveAll(workers);
    }

    @Override
    public void updateRole(PlaceRole role) {
        checkPermission(role.getId());

        roleRepository.save(role);
    }

    @Override
    public List<PlaceRole> getAllRoles() {
        var place = ctx.getPlace();
        return roleRepository.findAllByPlaceId(place.getId());
    }

    private void checkPermission(long roleId) {
        var role = roleRepository.findPlaceRoleById(roleId);
        var place = ctx.getPlace();
        if (place.getId() != role.getPlace().getId()) {
            throw new PermissionDeniedException();
        }
    }
}
