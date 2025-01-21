package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.adapter;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantEntityAdapter implements RestaurantPersistencePort {

    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final UserEntityRepository userEntityRepository;

    @Override
    public List<Restaurant> findAllRestaurants() {
        return restaurantEntityRepository.findAll().stream().map(restaurantEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Restaurant> getRestaurantByUuid(UUID uuid) {
        return restaurantEntityRepository.findById(uuid).map(restaurantEntityMapper::toDomain);
    }

    @Override
    public Optional<Restaurant> getRestaurantByName(String name) {
        return restaurantEntityRepository.findByName(name).map(restaurantEntityMapper::toDomain);
    }

    @Override
    public boolean restaurantExistsByNameAndAddressAndOwnerUuid(String name, String address, UUID ownerUuid) {
        return restaurantEntityRepository.existsByNameAndAddressAndOwnerUuid(name, address, ownerUuid);
    }

    @Override
    public UUID createRestaurant(Restaurant restaurant) {
        UserEntity ownerEntity = userEntityRepository.findById(restaurant.getOwnerUuid()).orElseThrow(() -> new UserDoesNotExistException(restaurant.getOwnerUuid()));
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        restaurantEntity.assignOwner(ownerEntity);
        return restaurantEntityRepository.save(restaurantEntity).getUuid();
    }
}
