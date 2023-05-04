package com.zaiika.placeservice.model;

import lombok.Builder;

@Builder
public record TokenDto(
        String token
) {
}
