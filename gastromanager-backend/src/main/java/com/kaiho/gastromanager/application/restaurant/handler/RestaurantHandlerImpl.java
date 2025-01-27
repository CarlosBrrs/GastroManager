package com.kaiho.gastromanager.application.restaurant.handler;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.application.restaurant.mapper.RestaurantMapper;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {

    private final RestaurantServicePort restaurantServicePort;
    private final RestaurantMapper restaurantMapper;

    @Override
    public ApiGenericResponse<List<RestaurantResponseDto>> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantServicePort.getAllRestaurants();
        var restaurantResponseDtoList = restaurantList.stream().map(restaurantMapper::toResponse).toList();
        return buildSuccessResponse("List of restaurants retrieved successfully", restaurantResponseDtoList);
    }

    @Override
    public ApiGenericResponse<RestaurantResponseDto> getRestaurantByUUID(UUID uuid) {
        Restaurant restaurantById = restaurantServicePort.getRestaurantById(uuid);
        RestaurantResponseDto response = restaurantMapper.toResponse(restaurantById);
        return buildSuccessResponse("Restaurant retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> createRestaurant(RestaurantRequestDto restaurantRequestDto, UUID uuid) {
        Restaurant restaurant = restaurantMapper.toDomain(restaurantRequestDto, uuid);
        UUID restaurantUuid = restaurantServicePort.createRestaurant(restaurant);
        return buildSuccessResponse("Restaurant added successfully", restaurantUuid);
    }

    @Override
    public ApiGenericResponse<RestaurantResponseDto> updateRestaurant(UUID uuid, RestaurantRequestDto restaurantRequestDto, UUID ownerUuid) {
        Restaurant restaurant = restaurantMapper.toDomain(restaurantRequestDto, ownerUuid);
        Restaurant updateRestaurant = restaurantServicePort.updateRestaurant(uuid, restaurant);
        RestaurantResponseDto response = restaurantMapper.toResponse(updateRestaurant);
        return buildSuccessResponse("Ingredient updated successfully", response);
    }
}
