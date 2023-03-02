package com.project.zaiika.models;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "site_id")
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
