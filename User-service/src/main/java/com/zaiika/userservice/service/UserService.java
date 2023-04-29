package com.zaiika.userservice.service;

import com.zaiika.userservice.model.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    void setRoleToUser(Long userId, String role);

    void deleteRoleFromUser(long userId, String role);

    void deleteUser(Long userId);
}
