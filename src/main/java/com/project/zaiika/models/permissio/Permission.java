package com.project.zaiika.models.permissio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.roles.PlaceRole;
import com.project.zaiika.models.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonInclude
    @JsonIgnore
    private List<PlaceRole> placeRoles;

    @ManyToMany(mappedBy = "permissions")
    @JsonInclude
    @JsonIgnore
    private List<Role> roles;

    public Permission(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
