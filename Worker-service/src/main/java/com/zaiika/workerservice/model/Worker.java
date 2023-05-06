package com.zaiika.workerservice.model;

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
    @Column(name = "id")
    private long id;

    private long placeId;
    private long userId;
    private long placeRoleId;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonIgnore
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "place_id")
//    @JsonIgnore
//    private Place place;
//
//    @ManyToOne
//    @JoinColumn(name = "place_role_id")
//    private PlaceRole placeRole;
}