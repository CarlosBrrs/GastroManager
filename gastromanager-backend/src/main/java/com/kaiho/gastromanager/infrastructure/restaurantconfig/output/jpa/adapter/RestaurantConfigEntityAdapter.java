package com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.adapter;

import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurantconfig.spi.RestaurantConfigPersistencePort;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.mapper.RestaurantConfigEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.repository.RestaurantConfigEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class RestaurantConfigEntityAdapter implements RestaurantConfigPersistencePort {

    private final RestaurantConfigEntityMapper restaurantConfigEntityMapper;
    private final RestaurantConfigEntityRepository restaurantConfigEntityRepository;

    @Override
    public Optional<RestaurantConfig> getConfigsForRestaurant(UUID currentRestaurant) {
        return restaurantConfigEntityRepository.findByRestaurantUuid(currentRestaurant).map(restaurantConfigEntityMapper::toDomain);
    }
}
