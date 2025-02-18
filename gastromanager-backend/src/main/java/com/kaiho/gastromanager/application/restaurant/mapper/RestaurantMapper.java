package com.kaiho.gastromanager.application.restaurant.mapper;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantConfigRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantConfigResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {

    private final RestaurantServicePort restaurantServicePort;

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

    public RestaurantConfig toDomain(RestaurantConfigRequestDto restaurantConfigRequestDto) {
        if (restaurantConfigRequestDto == null) {
            return null;
        }
        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());
        return RestaurantConfig.builder()
                .isPaymentRequiredBeforePlacement(restaurantConfigRequestDto.requirePaymentBeforeOrder())
                .restaurant(restaurant)
                .build();
    }

    public RestaurantConfigResponseDto toResponse(RestaurantConfig restaurantConfig) {
        if (restaurantConfig == null) {
            return null;
        }
        return RestaurantConfigResponseDto.builder()
                .uuid(restaurantConfig.getUuid())
                .requirePaymentBeforeOrder(restaurantConfig.isPaymentRequiredBeforePlacement())
                .restaurantName(restaurantConfig.getRestaurant().getName())
                .build();
    }
}
