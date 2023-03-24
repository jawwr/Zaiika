package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zaiika.models.placeModels.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    @Column(name = "place_id")
//    private long placeId;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @JsonBackReference
    private Place place;
}
