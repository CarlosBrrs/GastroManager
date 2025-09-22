package com.kaiho.gastromanager.domain.restaurantconfig.spi;

import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantConfigPersistencePort {
    Optional<RestaurantConfig> getConfigsForRestaurant(UUID currentRestaurant);
}
