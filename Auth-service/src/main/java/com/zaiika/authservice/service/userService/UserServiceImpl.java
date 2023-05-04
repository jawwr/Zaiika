package com.zaiika.authservice.service.userService;

import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.model.user.role.Role;
import com.zaiika.authservice.repository.PermissionRepository;
import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.repository.UserJpaRepository;
import com.zaiika.authservice.service.jwtService.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final TokenService tokenService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void setRoleToUser(Long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = findRoleByName(roleName);

        user.getRoles().add(role);
        userRepository.save(user);
    }

    private Role findRoleByName(String roleName) {
        var role = roleRepository.findRoleByName(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Role does not exist");
        }

        return role;
    }

    @Override
    public void deleteRoleFromUser(long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = findRoleByName(roleName);

        var roles = user.getRoles();
        var userRole = roles.stream().filter(x -> x.getId() == role.getId()).findFirst().orElse(null);
        if (userRole == null) {
            throw new IllegalArgumentException("User does not have role with name " + roleName);
        }

        roles.remove(userRole);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public boolean hasUserPermission(String token, String permissionName) {
        token = token.substring(7);
        var user = tokenService.getUserByToken(token);
        return permissionRepository.hasPermission(user.getId(), permissionName);
    }
}
