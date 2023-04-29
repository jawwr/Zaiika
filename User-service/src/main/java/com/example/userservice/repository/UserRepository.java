package com.example.userservice.repository;

import com.example.userservice.model.User;

import java.util.List;

public interface UserRepository {
    User getUserByLogin(String login);

    User getUserById(long id);

    boolean existUserByLogin(String login);

    User save(User user);

    List<User> getAllUsers();

    void deleteUserById(long id);
}
