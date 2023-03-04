package com.project.zaiika.services;

import com.project.zaiika.models.User;

public interface UserService {
    User getUserInfo();
    User getUserByLogin(String login);
}
