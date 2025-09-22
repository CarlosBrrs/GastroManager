package com.kaiho.gastromanager.domain.restaurantconfig.api;

import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;

import java.util.UUID;

public interface RestaurantConfigServicePort {
    RestaurantConfig getConfigsForRestaurant(UUID currentRestaurant);
}
