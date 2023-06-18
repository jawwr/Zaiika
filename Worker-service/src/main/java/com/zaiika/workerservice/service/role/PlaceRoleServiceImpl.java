package com.zaiika.workerservice.service.role;

import com.zaiika.workerservice.model.PlaceRole;
import com.zaiika.workerservice.model.permission.Permission;
import com.zaiika.workerservice.repository.PlaceRoleRepository;
import com.zaiika.workerservice.repository.WorkerRepository;
import com.zaiika.workerservice.service.permission.PermissionService;
import com.zaiika.workerservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceRoleServiceImpl implements PlaceRoleService {
    private final PlaceRoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final UserService userService;
    private final PermissionService permissionService;

    @Override
    public PlaceRole createRole(PlaceRole role) {
        role.setPlaceId(getPlaceId());
        if (role.getPermissions() != null) {
            checkPermissionsExisting(role.getPermissions());
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(long roleId) {
        var role = roleRepository.findPlaceRoleById(roleId);
        if (role == null){
            throw new IllegalArgumentException("Role with id " + roleId + " not exist");
        }
        if (role.getPlaceId() != getPlaceId()) {
            throw new IllegalArgumentException("Not enough permission");
        }
        var workers = workerRepository.findAllByPlaceRoleId(roleId);
        workers.forEach(x -> x.setPlaceRole(null));
        workerRepository.saveAll(workers);
        roleRepository.deleteRoleById(roleId);
    }

    @Override
    public PlaceRole updateRole(PlaceRole role) {
        var savedRole = roleRepository.findPlaceRoleById(role.getId());
        if (savedRole == null) {
            throw new IllegalArgumentException("Role with id " + role.getId() + " not exist");
        }
        if (savedRole.getPlaceId() != getPlaceId()) {
            throw new IllegalArgumentException("Not enough permission");
        }
        role.setPlaceId(savedRole.getPlaceId());
        validateRolePermissions(role);
        return roleRepository.save(role);
    }

    @Override
    public List<PlaceRole> getAllRoles() {
        return roleRepository.getAllByPlaceId(getPlaceId());
    }

    private long getPlaceId() {
        var user = userService.getUser();
        return user.placeId();
    }

    private void validateRolePermissions(PlaceRole role) {
        if (role.getPermissions() != null) {
            checkPermissionsExisting(role.getPermissions());
            return;
        }
        var savedRole = roleRepository.findPlaceRoleById(role.getId());
        role.setPermissions(savedRole.getPermissions());
    }

    private void checkPermissionsExisting(List<Permission> permissions) {
        for (Permission permission : permissions) {
            if (!permissionService.isPermissionExists(permission)) {
                throw new IllegalArgumentException("Permission with name '" + permission.getName() + "' not exists");
            }
        }
    }
}
