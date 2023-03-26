package com.project.zaiika.models.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.models.placeModels.ProductModification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("orderItem")
    private Product product;

    @ManyToMany
    @JoinTable(
            name = "order_item_exclude_ingredient",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "exclude_ingredient_id")
    )
    @JsonIgnoreProperties("orders")
    private List<Ingredient> excludeIngredient;

    @ManyToMany
    @JoinTable(
            name = "order_item_modification",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "modification_id")
    )
    @JsonIgnoreProperties("orders")
    private List<ProductModification> modifications;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
