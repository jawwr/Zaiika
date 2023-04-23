package com.project.zaiika.services.userServices;

import com.project.zaiika.models.permission.Permission;
import com.project.zaiika.models.roles.Role;
import com.project.zaiika.models.user.UserDetailImpl;
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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findUserByLogin(login);

        List<Role> userRoles = getRoles(user.getId());
        userRoles.addAll(getWorkerRole(user.getId()));
        user.setRoles(userRoles);

        return UserDetailImpl.of(user);
    }

    private List<Role> getRoles(long userId) {
        return roleRepository.findUserRole(userId);
    }

    private List<Role> getWorkerRole(long userId) {
        List<Role> roles = new ArrayList<>();
        if (!workerRepository.isWorkerExist(userId)) {
            return roles;
        }
        var worker = workerRepository.findWorkerByUserId(userId);
        var placeRole = worker.getPlaceRole();
        for (Permission permission : placeRole.getPermissions()) {
            roles.add(new Role(permission.getName()));
        }
        return roles;
    }
}
