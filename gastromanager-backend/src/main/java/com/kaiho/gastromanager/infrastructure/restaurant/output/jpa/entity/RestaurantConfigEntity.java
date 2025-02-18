package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Table(name = "restaurant_configs")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantConfigEntity extends Auditable implements Serializable {

    private boolean requirePaymentBeforeOrder;
    @OneToOne
    @JoinColumn(name = "restaurant_uuid", nullable = false, referencedColumnName = "uuid")
    private RestaurantEntity restaurant;
}
