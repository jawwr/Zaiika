package com.zaiika.authservice.service.userService;

import com.zaiika.authservice.model.user.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void setRoleToUser(Long userId, String role);

    void deleteRoleFromUser(long userId, String role);

    void deleteUser(Long userId);

    boolean hasUserRole(String token, String roleName);

    User getUserInfo(String token);
}
