package com.zaiika.placeservice.model;

import lombok.Builder;

@Builder
public record UserDto(
        long id,
        String name,
        String surname,
        String patronymic,
        String login
) {
}
