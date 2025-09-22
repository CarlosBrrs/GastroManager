package com.kaiho.gastromanager.domain.restaurant.api;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;

import java.util.List;
import java.util.UUID;

public interface RestaurantServicePort {
    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurantById(UUID uuid);

    UUID createRestaurant(Restaurant restaurant);

    Restaurant updateRestaurant(UUID uuid, Restaurant restaurant);

    RestaurantConfig createRestaurantConfig(RestaurantConfig restaurantConfig);

    RestaurantConfig getRestaurantConfig();

    List<UserRestaurantAccess> getUserRestaurants(UUID userUuid);

    Restaurant getRestaurantDetailsWithAccess(UUID restaurantUuid);
}
