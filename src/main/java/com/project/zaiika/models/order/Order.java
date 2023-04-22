package com.project.zaiika.models.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.zaiika.models.delivery.Delivery;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.worker.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude
    private long id;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    @JsonBackReference
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    @JsonBackReference(value = "delivery ref")
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @JsonBackReference(value = "placeOrder")
    @JsonIgnore
    private Place place;

    @Column(name = "date")
    @JsonInclude
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @Column(name = "is_cancelled", nullable = false)
    @JsonInclude
    private boolean isCanceled;
}
