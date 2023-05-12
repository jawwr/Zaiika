package com.zaiika.placeservice.service.users;

import com.zaiika.placeservice.model.utils.UserDto;

public interface UserService {
    UserDto getUser();

    void setRole(String roleName);

    void deleteRole(String roleName);
}
