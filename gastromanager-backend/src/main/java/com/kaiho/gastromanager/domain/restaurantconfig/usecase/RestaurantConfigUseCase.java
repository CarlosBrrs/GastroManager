package com.kaiho.gastromanager.domain.restaurantconfig.usecase;

import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurantconfig.api.RestaurantConfigServicePort;
import com.kaiho.gastromanager.domain.restaurantconfig.spi.RestaurantConfigPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class RestaurantConfigUseCase implements RestaurantConfigServicePort {

    private final RestaurantConfigPersistencePort restaurantConfigPersistencePort;

    @Override
    public RestaurantConfig getConfigsForRestaurant(UUID currentRestaurant) {
        return restaurantConfigPersistencePort.getConfigsForRestaurant(currentRestaurant).orElseThrow(() -> new IllegalArgumentException("Configs not found"));
    }
}
