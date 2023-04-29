package com.zaiika.userservice.service;

import com.zaiika.userservice.model.User;
import com.zaiika.userservice.model.UserDto;
import com.zaiika.userservice.model.roles.Role;
import com.zaiika.userservice.repository.RoleRepository;
import com.zaiika.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserDto> getAllUsers() {
        var users = userRepository.getAllUsers();

        return users.stream().map(this::convertUserToDto).toList();
    }

    private UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .login(user.getLogin())
                .role(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Override
    public void setRoleToUser(Long userId, String roleName) {
        var user = userRepository.getUserById(userId);
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
        var user = userRepository.getUserById(userId);
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
}
