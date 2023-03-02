package com.project.zaiika.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ingredients")
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ingredient_id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "netWeight")
    private int netWeight;

    @Column(name = "grossWeight")
    private int grossWeight;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
