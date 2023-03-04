package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.User;

public interface UserService {
    User getUserInfo();
    User getUserByLogin(String login);
}
