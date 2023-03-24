package com.project.zaiika.models.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkerDto {
    @JsonInclude
    private long id;
    private String name;
    private String surname;
    private String patronymic;
    private String pinCode;
    private long placeRoleId;
    @JsonInclude
    private long placeId;
    @JsonInclude
    private String role;
}
