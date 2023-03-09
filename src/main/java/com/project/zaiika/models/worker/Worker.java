package com.project.zaiika.models.worker;

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

    @Column(name = "user_id")
    private long userId;

    @Column(name = "place_id")
    private long placeId;

    @Column(name = "place_role_id")
    private long placeRoleId;

    @Column(name = "pin_code")
    private String pinCode;
}