package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantConfigEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

    public RestaurantConfigEntity toEntity(RestaurantConfig restaurantConfig) {
        if (restaurantConfig == null) {
            return null;
        }
        return RestaurantConfigEntity.builder()
                .requirePaymentBeforeOrder(restaurantConfig.isPaymentRequiredBeforePlacement())
                .build();
    }

    public RestaurantConfig toDomain(RestaurantConfigEntity restaurantConfigEntity) {
        if (restaurantConfigEntity == null) {
            return null;
        }
        Restaurant restaurant = toDomain(restaurantConfigEntity.getRestaurant());
        return RestaurantConfig.builder()
                .uuid(restaurantConfigEntity.getUuid())
                .restaurant(restaurant)
                .isPaymentRequiredBeforePlacement(restaurantConfigEntity.isRequirePaymentBeforeOrder())
                .build();
    }
}
