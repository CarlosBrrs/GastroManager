package com.kaiho.gastromanager.infrastructure.config.context;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantContext {
    private static final ThreadLocal<UUID> currentRestaurantUuid = new ThreadLocal<>();

    private RestaurantContext() {
    }

    public static void setCurrentRestaurant(UUID restaurantUuid) {
        currentRestaurantUuid.set(restaurantUuid);
    }

    public static UUID getCurrentRestaurant() {
        return currentRestaurantUuid.get();
    }

    public static void clear() {
        currentRestaurantUuid.remove();
    }
}