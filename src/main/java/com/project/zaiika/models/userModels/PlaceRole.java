package com.project.zaiika.models.userModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.worker.Worker;
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

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @JsonIgnore
    private Place place;

    @OneToMany(mappedBy = "placeRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude
    @JsonIgnore
    private List<Worker> workers;
}
