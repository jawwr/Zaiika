package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDto;
import com.project.zaiika.repositories.role.RoleRepository;
import com.project.zaiika.repositories.user.UserRepository;
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
        var users = userRepository.findAll();

        return users.stream().map(this::convertUserToDto).toList();
    }

    private UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .login(user.getLogin())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public void changeUserRole(Long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = roleRepository.findRoleByNameIgnoreCase(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Role does not exist");
        }

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
