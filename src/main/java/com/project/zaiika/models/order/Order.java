package com.project.zaiika.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.zaiika.models.placeModels.Ingredient;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.models.placeModels.ProductModification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "worker_id")
    @JsonInclude
    private long workerId;

    @Column(name = "delivery_type_id")
    private long deliveryTypeId;

//    @Column(name = "place_id")
//    @JsonInclude
//    private long placeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
//    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<Product> products;

    @ManyToMany
    @JoinTable(
            name = "order_exclude_ingredient",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "exclude_ingredient_id")
    )
//    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<Ingredient> excludeIngredient;

    @ManyToMany
    @JoinTable(
            name = "order_modification",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "modification_id")
    )
//    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<ProductModification> modifications;

    @Column(name = "date")
    @JsonInclude
    private LocalDateTime date;

    @Column(name = "is_cancelled", nullable = false)
    @JsonInclude
    private boolean isCancelled;
}
