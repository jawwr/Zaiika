package com.zaiika.workerservice.service.permission;

import com.zaiika.workerservice.model.permission.Permission;
import com.zaiika.workerservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(long id) {
        var permission = permissionRepository.getPermissionById(id);
        if (permission == null) {
            throw new IllegalArgumentException("Permission with id " + id + "' not exists");
        }
        return permission;
    }

    @Override
    public boolean isPermissionExists(Permission permission) {
        return permissionRepository.isPermissionExists(permission.getName());
    }
}
