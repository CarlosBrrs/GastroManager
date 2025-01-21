package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEntityMapper {
    public Restaurant toDomain(RestaurantEntity restaurantEntity) {
        if (restaurantEntity == null) {
            return null;
        }
        return Restaurant.builder()
                .uuid(restaurantEntity.getUuid())
                .name(restaurantEntity.getName())
                .description(restaurantEntity.getDescription())
                .address(restaurantEntity.getAddress())
                .ownerUuid(restaurantEntity.getOwner().getUuid())
                .build();
    }

    public RestaurantEntity toEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        return RestaurantEntity.builder()
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .build();
    }
}
