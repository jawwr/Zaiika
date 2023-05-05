package com.zaiika.placeservice.model.utils;

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
