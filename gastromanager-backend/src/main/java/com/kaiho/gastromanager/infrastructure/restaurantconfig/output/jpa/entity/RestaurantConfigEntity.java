package com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.Entity;
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
//@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@AllArgsConstructor
@Getter
@Setter
public class RestaurantConfigEntity extends Auditable implements Serializable {

    private boolean payBeforeOrder;
    private boolean isFranchise;
    private boolean enableDelivery;

    @OneToOne(mappedBy = "restaurantConfig")
    private RestaurantEntity restaurant;
}
