package com.project.zaiika.models.userModels;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id, String name, String surname, String patronymic, String login, List<String> role) {
}
