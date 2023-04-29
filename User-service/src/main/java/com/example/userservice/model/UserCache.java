package com.example.userservice.model;

import com.example.userservice.model.permission.Permission;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record UserCache(
        long id,
        String name,
        String surname,
        String patronymic,
        String login,
        String password,
        List<Permission> permissions
) implements Serializable {
}
