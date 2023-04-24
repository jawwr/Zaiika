package com.project.zaiika.models.user;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id, String name, String surname, String patronymic, String login, List<String> role) {
}
