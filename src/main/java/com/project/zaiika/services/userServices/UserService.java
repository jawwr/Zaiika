package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    void addUserRole(Long userId, String role);
    void deleteUserRole(long userId, String role);

    void deleteUser(Long userId);
}
