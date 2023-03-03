package com.project.zaiika.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "sites")
@Entity
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id", nullable = false)
    private long id;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Menu> menu;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
