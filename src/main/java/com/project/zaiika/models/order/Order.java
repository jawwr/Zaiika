package com.project.zaiika.models.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "place_id")
    @JsonInclude
    private long placeId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderProductDto> products;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderIngredientDto> excludeIngredient;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderModificationDto> modifications;

    @Column(name = "date")
    @JsonInclude
    private LocalDateTime date;

    public void setDependency() {
        setProductsDependency();
        setExcludeIngredientDependency();
        setModificationDependency();
    }

    private void setProductsDependency() {
        for (OrderProductDto product : this.products) {
            product.setOrder(this);
        }
    }

    private void setExcludeIngredientDependency() {
        for (OrderIngredientDto ingredient : this.excludeIngredient) {
            ingredient.setOrder(this);
        }
    }

    private void setModificationDependency() {
        for (OrderModificationDto modification : this.modifications) {
            modification.setOrder(this);
        }
    }
}
