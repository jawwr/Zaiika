package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.worker.WorkerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

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
        this.password = workerDto.getPinCode();
        this.login = generateLogin(workerDto);
    }

    private String generateLogin(WorkerDto dto) {
        Random random = new Random();
        return "w" +
                random.nextInt(1000) +
                dto.getName().toCharArray()[0] +
                dto.getName().toCharArray()[dto.getName().length() - 1] +
                dto.getName().length() +
                dto.getSurname().toCharArray()[0] +
                dto.getSurname().toCharArray()[dto.getSurname().length() - 1] +
                dto.getSurname().length() +
                dto.getPatronymic().toCharArray()[0] +
                dto.getPatronymic().toCharArray()[dto.getPatronymic().length() - 1] +
                dto.getPatronymic().length() +
                dto.getPlaceId();
    }
}
