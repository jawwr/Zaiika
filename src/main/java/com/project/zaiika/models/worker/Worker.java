package com.project.zaiika.models.worker;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.userModels.PlaceRole;
import com.project.zaiika.models.userModels.User;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "worker_id")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "place_role_id", nullable = false)
    private PlaceRole placeRole;
}