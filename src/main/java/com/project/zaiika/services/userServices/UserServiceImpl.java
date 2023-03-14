package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDto;
import com.project.zaiika.repositories.userRepositories.RoleRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        var roles = roleRepository.findAll();

        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            var role = roles.stream().filter(r -> r.getId() == user.getRoleId()).findFirst().get();
            dtos.add(UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .patronymic(user.getPatronymic())
                    .login(user.getLogin())
                    .role(role.getName())
                    .build()
            );
        }
        return dtos;
    }

    @Override
    public void changeUserRole(Long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = roleRepository.findRoleByNameIgnoreCase(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Role does not exist");
        }

        user.setRole(role);
        user.setRoleId(role.getId());
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
