package com.zaiika.workerservice.model;

public record UserDto(
        long id,
        String name,
        String surname,
        String patronymic,
        String login
) {
}
