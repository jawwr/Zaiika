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

    @JsonIgnore
    private long placeId;

    @Column(name = "user_id")
    private long userId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "place_role_id")
    private PlaceRole placeRole;
}