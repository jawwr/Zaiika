package com.project.zaiika.services.userServices;

import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.models.roles.Role;
import com.project.zaiika.models.user.UserDetailImpl;
import com.project.zaiika.repositories.permissions.PermissionRepository;
import com.project.zaiika.repositories.role.RoleRepository;
import com.project.zaiika.repositories.user.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.getUserByLogin(login);

        user.setRoles(getRoles(user.getId()));

        List<Permission> permissions = getPermissions(user.getId());
        permissions.addAll(getWorkerPermission(user.getId()));

        user.setPermissions(permissions);

        return UserDetailImpl.of(user);
    }

    private List<Role> getRoles(long userId) {
        return roleRepository.findUserRole(userId);
    }

    private List<Permission> getPermissions(long userId) {
        return permissionRepository.getUserPermission(userId);
    }

    private List<Permission> getWorkerPermission(long userId) {
        List<Permission> permissions = new ArrayList<>();
        if (!workerRepository.isWorkerExist(userId)) {
            return permissions;
        }
        var worker = workerRepository.findWorkerByUserId(userId);
        var placeRole = worker.getPlaceRole();

        permissions.addAll(placeRole.getPermissions());
        return permissions;
    }
}
