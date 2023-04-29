package com.zaiika.deliveryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude
    private long id;

    @Column(name = "type")
    private String deliveryType;

//    @ManyToOne
//    @JoinColumn(name = "place_id", nullable = false)
//    @JsonIgnore
//    private Place place;
    private long placeId;
}
