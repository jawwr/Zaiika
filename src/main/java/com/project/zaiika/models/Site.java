package com.project.zaiika.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sites")
@Entity
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id", nullable = false)
    @JsonInclude
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "site_menu",
            joinColumns =
                    { @JoinColumn(name = "site_id", referencedColumnName = "site_id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "menu_id", referencedColumnName = "menu_id") })
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
