package com.example.workerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private long id;
    private String name;
    private String surname;
    private String patronymic;
    @JsonIgnore
    private String pinCode;
    @JsonIgnore
    private long placeRoleId;
    @JsonInclude
    @JsonIgnore
    private long placeId;
    @JsonInclude
    private String role;
}
