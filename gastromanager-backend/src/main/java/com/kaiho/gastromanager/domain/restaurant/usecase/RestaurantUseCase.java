package com.kaiho.gastromanager.domain.restaurant.usecase;

import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantAlreadyExistsException;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        return restaurantPersistencePort.findAllRestaurants();
    }

    @Override
    public Restaurant getRestaurantById(UUID uuid) {
        // todo: delete when validation in requestdto is enabled
        if (uuid == null) {
            throw new IllegalArgumentException("UUID for restaurant cannot be null");
        }
        // todo: delete when validation in requestdto is enabled

        return restaurantPersistencePort.getRestaurantByUuid(uuid)
                .orElseThrow(() -> new RestaurantDoesNotExistException(uuid.toString()));
    }

    @Override
    public UUID createRestaurant(Restaurant restaurant) {
        if (restaurantPersistencePort.restaurantExistsByNameAndAddressAndOwnerUuid(restaurant.getName(), restaurant.getAddress(), restaurant.getOwnerUuid())) {
            throw new RestaurantAlreadyExistsException(restaurant.getName(), restaurant.getAddress(), restaurant.getOwnerUuid().toString());
        }
        return restaurantPersistencePort.createRestaurant(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(UUID uuid, Restaurant restaurant) {
        return null;
    }
}
