package com.kaiho.gastromanager.application.restaurant.handler;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantHandler {
    ApiGenericResponse<List<RestaurantResponseDto>> getAllRestaurants();

    ApiGenericResponse<RestaurantResponseDto> getRestaurantByUUID(UUID uuid);

    ApiGenericResponse<UUID> createRestaurant(RestaurantRequestDto restaurantRequestDto, UUID uuid);

    ApiGenericResponse<RestaurantResponseDto> updateRestaurant(UUID restaurantUuid, RestaurantRequestDto restaurantRequestDto, UUID ownerUuid);
}
