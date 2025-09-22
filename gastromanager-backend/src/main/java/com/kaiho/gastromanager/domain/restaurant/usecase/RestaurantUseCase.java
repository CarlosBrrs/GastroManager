package com.kaiho.gastromanager.domain.restaurant.usecase;

import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantAlreadyExistsException;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public RestaurantConfig createRestaurantConfig(RestaurantConfig restaurantConfig) {
        validateRestaurantConfigCreation(restaurantConfig);
        return restaurantPersistencePort.createRestaurantConfig(restaurantConfig);
    }

    @Override
    public RestaurantConfig getRestaurantConfig() {
        return restaurantPersistencePort.getRestaurantConfigByRestaurantUuid(getCurrentRestaurant());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRestaurantAccess> getUserRestaurants(UUID userUuid) {
        if (userUuid == null) {
            throw new IllegalArgumentException("User UUID cannot be null");
        }

        log.info("Fetching restaurants for user: {}", userUuid);
        return restaurantPersistencePort.findRestaurantsByUser(userUuid);
    }

    private boolean hasUserAccessToRestaurant(UUID userUuid, UUID restaurantUuid) {
        if (userUuid == null) {
            throw new IllegalArgumentException("User UUID cannot be null");
        }
        if (restaurantUuid == null) {
            throw new IllegalArgumentException("Restaurant UUID cannot be null");
        }

        List<UserRestaurantAccess> userRestaurants = restaurantPersistencePort.findRestaurantsByUser(userUuid);
        return userRestaurants.stream()
                              .anyMatch(restaurant -> restaurant.getUuid().equals(restaurantUuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurantDetailsWithAccess(UUID restaurantUuid) {
        if (restaurantUuid == null) {
            throw new IllegalArgumentException("Restaurant UUID cannot be null");
        }

        // Obtener el usuario autenticado desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        // Validar que el usuario tenga acceso al restaurante
        boolean hasAccess = hasUserAccessToRestaurant(principal.getUuid(), restaurantUuid);
        if (!hasAccess) {
            // Lanzar la misma excepción que si el restaurante no existiera
            // Esto es una práctica de seguridad para no revelar la existencia de recursos restringidos
            throw new RestaurantDoesNotExistException(restaurantUuid.toString());
        }

        // Si tiene acceso, proceder con la consulta normal
        return restaurantPersistencePort.getRestaurantByUuid(restaurantUuid)
                                        .orElseThrow(() -> new RestaurantDoesNotExistException(restaurantUuid.toString()));
    }

    private void validateRestaurantConfigCreation(RestaurantConfig restaurantConfig) {
        log.info("Here to validate info for " + restaurantConfig);
    }
}
