package com.project.zaiika.models.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.order.Order;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.roles.PlaceRole;
import com.project.zaiika.models.userModels.User;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "place_role_id")
    private PlaceRole placeRole;

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude
    private List<Order> order;
}