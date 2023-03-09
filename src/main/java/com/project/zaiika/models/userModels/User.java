package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.worker.Worker;
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

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id")
    private long roleId;

    @JsonInclude
    @Transient
    private Role role;

    public User(Worker worker) {
        this.id = worker.getId();
        this.name = worker.getName();
        this.surname = worker.getSurname();
        this.login = worker.getLogin();
        this.password = worker.getPassword();
        this.roleId = worker.getPlaceRole();
        this.role = new Role(4L, UserRole.WORKER.name());
    }
}
