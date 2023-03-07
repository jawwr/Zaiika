package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.repositories.userRepositories.PlaceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final PlaceRoleRepository repository;

    @Autowired
    public RoleServiceImpl(PlaceRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createRole(PlaceRole role) {
        repository.save(role);
    }

    @Override
    public void deleteRole(long placeId, long roleId) {
        repository.deleteRoleById(roleId);
    }

    @Override
    public void updateRole(PlaceRole role) {
        repository.updateRole(role);
    }

    @Override
    public List<PlaceRole> getAllRoles(long placeId) {
        return repository.findAllByPlaceId(placeId);
    }
}
