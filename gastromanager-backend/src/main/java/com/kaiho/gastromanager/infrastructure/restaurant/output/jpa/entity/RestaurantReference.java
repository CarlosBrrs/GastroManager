package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantReference implements Serializable {

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid")
    private RestaurantEntity restaurant;

}