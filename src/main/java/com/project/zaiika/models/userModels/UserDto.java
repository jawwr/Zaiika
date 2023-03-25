package com.project.zaiika.models.userModels;

import lombok.Builder;

@Builder
public record UserDto(Long id, String name, String surname, String patronymic, String login, String role) {
}
