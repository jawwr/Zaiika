package com.zaiika.workerservice.model;

public record WorkerCredentials(
        long id,
        long placeId,
        String pin,
        String name,
        String surname,
        String patronymic,
        String login,
        String placeRole
) {
}
