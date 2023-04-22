package com.project.zaiika.models.worker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.order.Order;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.PlaceRole;
import com.project.zaiika.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @JsonIgnore
    private Place place;

    @ManyToOne
    @JoinColumn(name = "place_role_id")
    private PlaceRole placeRole;
}