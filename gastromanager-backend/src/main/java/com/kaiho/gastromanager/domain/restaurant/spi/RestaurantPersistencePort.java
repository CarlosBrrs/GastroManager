package com.kaiho.gastromanager.domain.restaurant.spi;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantPersistencePort {
    List<Restaurant> findAllRestaurants();

    Optional<Restaurant> getRestaurantByUuid(UUID uuid);

    boolean restaurantExistsByNameAndAddressAndOwnerUuid(String name, String address, UUID ownerUuid);

    UUID createRestaurant(Restaurant restaurant);


    Optional<Restaurant> getRestaurantByName(String restaurantName);

    RestaurantConfig createRestaurantConfig(RestaurantConfig restaurantConfig);

    RestaurantConfig getRestaurantConfigByRestaurantUuid(UUID currentRestaurant);

    List<UserRestaurantAccess> findRestaurantsByUser(UUID userUuid);
}