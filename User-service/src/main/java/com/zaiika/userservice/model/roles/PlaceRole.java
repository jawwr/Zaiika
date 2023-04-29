package com.zaiika.userservice.model.roles;

import com.zaiika.userservice.model.permission.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "place_roles")
public class PlaceRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    //    @ManyToOne
//    @JoinColumn(name = "place_id", nullable = false)
//    @JsonIgnore
//    private Place place;
    private long placeId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "place_role_permission",
            joinColumns = @JoinColumn(name = "place_role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnoreProperties("placeRoles")
    private List<Permission> permissions;
}
