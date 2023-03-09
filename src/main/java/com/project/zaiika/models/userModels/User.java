package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.worker.WorkerDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id")
    private long roleId;

    @JsonInclude
    @Transient
    private Role role;

    public User(WorkerDto workerDto) {
        this.name = workerDto.getName();
        this.surname = workerDto.getSurname();
        this.patronymic = workerDto.getPatronymic();
        this.role = new Role(4L, UserRole.WORKER.name());
        this.roleId = this.role.getId();
    }
}
