package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    void changeUserRole(Long userId, String role);

    void deleteUser(Long userId);
}
