package com.example.userservice.service;

import com.example.userservice.model.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    void setRoleToUser(Long userId, String role);

    void deleteRoleFromUser(long userId, String role);

    void deleteUser(Long userId);
}
