package com.kaiho.gastromanager.domain.restaurant.api;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantServicePort {
    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurantById(UUID uuid);

    UUID createRestaurant(Restaurant restaurant);

    Restaurant updateRestaurant(UUID uuid, Restaurant restaurant);
}
