package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.repositories.userRepositories.PlaceRoleRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final PlaceRoleRepository roleRepository;
    private final WorkerRepository workerRepository;

    @Override
    public void createRole(PlaceRole role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(long placeId, long roleId) {
        roleRepository.deleteRoleById(roleId);
        var workers = workerRepository.findAllByPlaceRoleId(roleId);
        for (Worker worker : workers) {
            worker.setPlaceRoleId(0);
        }
        workerRepository.saveAll(workers);
    }

    @Override
    public void updateRole(PlaceRole role) {
        roleRepository.updateRole(role);
    }

    @Override
    public List<PlaceRole> getAllRoles(long placeId) {
        return roleRepository.findAllByPlaceId(placeId);
    }
}
