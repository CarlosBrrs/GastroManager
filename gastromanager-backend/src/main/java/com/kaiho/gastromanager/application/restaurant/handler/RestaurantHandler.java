package com.kaiho.gastromanager.application.restaurant.handler;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantConfigRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantCreateRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantConfigResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantDetailResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.UserRestaurantAccessResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantHandler {
    ApiGenericResponse<List<RestaurantResponseDto>> getAllRestaurants();

    ApiGenericResponse<RestaurantResponseDto> getRestaurantByUUID(UUID uuid);

    ApiGenericResponse<UUID> createRestaurant(RestaurantCreateRequestDto restaurantCreateRequestDto);

    ApiGenericResponse<RestaurantResponseDto> updateRestaurant(UUID restaurantUuid, RestaurantRequestDto restaurantRequestDto, UUID ownerUuid);

    ApiGenericResponse<RestaurantConfigResponseDto> createRestaurantConfig(RestaurantConfigRequestDto restaurantRequestDto);

    ApiGenericResponse<RestaurantConfigResponseDto> getRestaurantConfig();

    ApiGenericResponse<List<UserRestaurantAccessResponseDto>> getUserRestaurants();

    ApiGenericResponse<RestaurantDetailResponseDto> getRestaurantDetails(UUID restaurantUuid);
}