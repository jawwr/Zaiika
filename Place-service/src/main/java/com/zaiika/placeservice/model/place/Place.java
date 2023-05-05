package com.zaiika.placeservice.model.place;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "places")
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonInclude
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner_id")
    private long ownerId;
}
