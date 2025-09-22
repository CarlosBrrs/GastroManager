package com.kaiho.gastromanager.application.restaurant.handler;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantConfigRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantCreateRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantConfigResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantDetailResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.UserRestaurantAccessResponseDto;
import com.kaiho.gastromanager.application.restaurant.mapper.RestaurantMapper;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ApiGenericResponse<UUID> createRestaurant(RestaurantCreateRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantMapper.toDomain(restaurantRequestDto);
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

    @Override
    public ApiGenericResponse<RestaurantConfigResponseDto> createRestaurantConfig(RestaurantConfigRequestDto restaurantConfigRequestDto) {
        /*RestaurantConfig restaurantConfig = restaurantMapper.toDomain(restaurantConfigRequestDto);
        RestaurantConfig configResponse = restaurantServicePort.createRestaurantConfig(restaurantConfig);
        RestaurantConfigResponseDto response = restaurantMapper.toResponse(configResponse);
        return buildSuccessResponse("Config for restaurant created successfully", response);*/
        return null;
    }

    @Override
    public ApiGenericResponse<RestaurantConfigResponseDto> getRestaurantConfig() {
        RestaurantConfigResponseDto response = restaurantMapper.toResponse(restaurantServicePort.getRestaurantConfig());
        return buildSuccessResponse("Config for restaurant retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<List<UserRestaurantAccessResponseDto>> getUserRestaurants() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        List<UserRestaurantAccess> userRestaurants = restaurantServicePort.getUserRestaurants(principal.getUuid());
        List<UserRestaurantAccessResponseDto> responseDto = userRestaurants.stream()
                                                                           .map(restaurantMapper::toUserRestaurantAccessResponse)
                                                                           .toList();

        return buildSuccessResponse("User restaurants retrieved successfully", responseDto);
    }

    @Override
    public ApiGenericResponse<RestaurantDetailResponseDto> getRestaurantDetails(UUID restaurantUuid) {
        Restaurant restaurant = restaurantServicePort.getRestaurantDetailsWithAccess(restaurantUuid);
        RestaurantDetailResponseDto response = restaurantMapper.toRestaurantDetailResponse(restaurant);
        return buildSuccessResponse("Restaurant details retrieved successfully", response);
    }
}
