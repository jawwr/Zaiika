package com.zaiika.workerservice.model;

public record WorkerCredentials(
        long id,
        long placeId,
        String pin
) {
}
