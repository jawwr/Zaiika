package com.project.zaiika.models.placeModels;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record PlaceCache(
        long  id,
        String name,
        long ownerId
) implements Serializable {
}
