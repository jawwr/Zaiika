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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "net_weight")
    private int netWeight;

    @Column(name = "gross_weight")
    private int grossWeight;

    @Column(name = "product_id", nullable = false)
    private long productId;
}
