package com.project.zaiika.models;

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
@Table(name = "places")
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "place_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sites")
    @OneToMany(mappedBy = "place")
    private List<Site> sites;
}
