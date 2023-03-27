package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.Role;
import com.project.zaiika.models.worker.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties("user")
    private List<Role> roles;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Worker worker;

    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Place place;//TODO может ли один юзер иметь несколько заведений?
}
