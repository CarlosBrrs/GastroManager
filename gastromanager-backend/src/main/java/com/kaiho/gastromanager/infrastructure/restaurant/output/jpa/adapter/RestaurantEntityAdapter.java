package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.adapter;

import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.AccessType;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.entity.RestaurantConfigEntity;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.mapper.RestaurantConfigEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.repository.RestaurantConfigEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantEntityAdapter implements RestaurantPersistencePort {

    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final RestaurantConfigEntityMapper restaurantConfigEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final RestaurantConfigEntityRepository restaurantConfigEntityRepository;

    @Override
    public List<Restaurant> findAllRestaurants() {
        return restaurantEntityRepository.findAll().stream().map(restaurantEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Restaurant> getRestaurantByUuid(UUID uuid) {
        Optional<Restaurant> restaurant = restaurantEntityRepository.findById(uuid).map(restaurantEntityMapper::toDomain);
        return restaurant;
    }

    @Override
    public Optional<Restaurant> getRestaurantByName(String name) {
        return restaurantEntityRepository.findByName(name).map(restaurantEntityMapper::toDomain);
    }

    @Override
    public RestaurantConfig createRestaurantConfig(RestaurantConfig restaurantConfig) {
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(restaurantConfig.getRestaurant().getUuid()).orElseThrow(() -> new RestaurantDoesNotExistException(restaurantConfig.getRestaurant().getUuid().toString()));
        RestaurantConfigEntity restaurantConfigEntity = restaurantConfigEntityMapper.toEntity(restaurantConfig);
        restaurantEntity.setConfigs(restaurantConfigEntity);
        RestaurantConfigEntity saved = restaurantConfigEntityRepository.save(restaurantConfigEntity);
        return restaurantConfigEntityMapper.toDomain(saved);
    }

    @Override
    public RestaurantConfig getRestaurantConfigByRestaurantUuid(UUID currentRestaurant) {
        return restaurantConfigEntityMapper.toDomain(restaurantConfigEntityRepository.findByRestaurantUuid(currentRestaurant).orElseThrow(() -> new IllegalArgumentException("no cofigs for the restaurant: " + currentRestaurant)));
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

    @Override
    public List<UserRestaurantAccess> findRestaurantsByUser(UUID userUuid) {
        // Obtener restaurantes donde el usuario es OWNER
        List<RestaurantEntity> ownedRestaurants = restaurantEntityRepository.findByOwnerUuid(userUuid);
        List<UserRestaurantAccess> ownerAccess = ownedRestaurants.stream()
                                                                 .map(restaurant -> restaurantEntityMapper.toUserRestaurantAccessFromEntity(restaurant, AccessType.OWNER))
                                                                 .toList();

        // Obtener restaurante donde el usuario es EMPLEADO
        Optional<UserEntity> userEntity = userEntityRepository.findById(userUuid);
        List<UserRestaurantAccess> employeeAccess = userEntity
                .filter(user -> user.getRestaurant() != null)
                .map(user -> restaurantEntityMapper.toUserRestaurantAccessFromEntity(user.getRestaurant(), AccessType.EMPLOYEE))
                .stream()
                .toList();

        // Combinar ambas listas
        List<UserRestaurantAccess> allAccess = new ArrayList<>(ownerAccess);
        allAccess.addAll(employeeAccess);

        return allAccess;
    }
}
