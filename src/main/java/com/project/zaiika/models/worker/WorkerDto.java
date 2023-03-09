package com.project.zaiika.models.worker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkerDto {
    private long id;
    private String name;
    private String surname;
    private String login;
    private long placeId;
    private String role;

    public WorkerDto(Worker worker) {
        this.id = worker.getId();
        this.login = worker.getLogin();
        this.name = worker.getName();
        this.surname = worker.getSurname();
        this.role = worker.getRole();
    }
}
