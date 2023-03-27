package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.Role;
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

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;//TODO сделать несколько ролей для юзера

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Worker worker;

    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Place place;//TODO может ли один юзер иметь несколько заведений?
}
