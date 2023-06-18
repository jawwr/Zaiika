package com.zaiika.workerservice.service.permission;

import com.zaiika.workerservice.model.permission.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAll();

    Permission getPermissionById(long id);
    boolean isPermissionExists(Permission permission);
    //TODO возможно управление правами (удаление, добавление)
}
