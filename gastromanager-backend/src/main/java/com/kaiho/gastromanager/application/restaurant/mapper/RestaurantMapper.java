package com.kaiho.gastromanager.application.restaurant.mapper;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMapper {
    public RestaurantResponseDto toResponse(Restaurant restaurant) {
        return null;
    }

    public Restaurant toDomain(RestaurantRequestDto restaurantRequestDto, UUID ownerUuid) {
        if (restaurantRequestDto == null) {
            return null;
        }
        return Restaurant.builder()
                .name(restaurantRequestDto.name())
                .address(restaurantRequestDto.address().street())
                .description(restaurantRequestDto.description())
                .ownerUuid(ownerUuid)
                .build();
    }
}
