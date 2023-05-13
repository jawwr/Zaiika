package com.zaiika.workerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "user_id")
    @JsonIgnore
    private long userId;

    @ManyToOne
    @JoinColumn(name = "place_role_id")
    private PlaceRole placeRole;
}